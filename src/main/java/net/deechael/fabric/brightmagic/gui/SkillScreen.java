package net.deechael.fabric.brightmagic.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillScreen extends Screen {

    private static void doNothing(ButtonWidget button) {}

    private final Screen parent;

    private final List<TexturedButtonWidget> texturedButtonWidgets = new ArrayList<>();

    private final Map<TexturedButtonWidget, Skill> skillMap = new HashMap<>();

    private ButtonWidget backButton;
    private ButtonWidget previousPage;
    private ButtonWidget nextPage;

    private int page = 1;

    private final Element element;

    public SkillScreen(Screen parent, Element element) {
        this(parent, element, Text.translatable("brightmagic.gui.element.title"));
    }

    public SkillScreen(Screen parent, Element element, Text title) {
        super(title);
        this.element = element;
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.texturedButtonWidgets.clear();
        this.skillMap.clear();

        int left = (this.width - (16 * 7)) / 2;
        int top = (this.height - (16 * 7)) / 2;

        List<Skill> skills = Skill.getSkills(element);
        int pages = skills.size() % 12 == 0 ? skills.size() / 12 : skills.size() / 12 + 1;
        if (this.page < 1)
            this.page = 1;
        if (this.page > pages)
            this.page = pages;
        for (int i = (page - 1) * 12; i < Math.min(skills.size(), pages * 12); i++) {
            int j = i - (page - 1) * 12;
            int x = left + ((j % 4) * 32);
            int y = top + ((j / 4) * 32);
            TexturedButtonWidget texturedButtonWidget = this.addDrawableChild(texture(skills.get(i), x, y));
            this.texturedButtonWidgets.add(texturedButtonWidget);
            this.skillMap.put(texturedButtonWidget, skills.get(i));
        }
        this.backButton = this.addDrawableChild(new ButtonWidget(16, 16, 16, 16, Text.literal("<"), button -> {
            this.close();
        }));
        this.previousPage = this.addDrawableChild(new ButtonWidget(left, top + 96, 16, 16, Text.literal("<"), button -> {
            this.page -= 1;
            this.clearAndInit();
        }));
        this.nextPage = this.addDrawableChild(new ButtonWidget(left + 96, top + 96, 16, 16, Text.literal(">"), button -> {
            this.page += 1;
            this.clearAndInit();
        }));
    }

    private TexturedButtonWidget texture(Skill skill, int x, int y) {
        return new TexturedButtonWidget(x, y, 16, 16, 0, 0, 16, skill.getTexture(), 16, 16, SkillScreen::doNothing);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        List<Skill> unlocked = this.client == null ? List.of() : List.of(SkillData.getUnlockedSkills((IDataHolder) this.client.player));
        for (TexturedButtonWidget texturedButtonWidget : this.texturedButtonWidgets) {
            texturedButtonWidget.render(matrices, mouseX, mouseY, delta);
            if (unlocked.contains(skillMap.get(texturedButtonWidget)))
                continue;
            if (this.client == null)
                continue;
            RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_DENY);
            DrawableHelper.drawTexture(matrices, texturedButtonWidget.x, texturedButtonWidget.y, 0, 0, 16, 16, 16, 16);
        }
        this.backButton.render(matrices, mouseX, mouseY, delta);
        this.previousPage.render(matrices, mouseX, mouseY, delta);
        this.nextPage.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

}
