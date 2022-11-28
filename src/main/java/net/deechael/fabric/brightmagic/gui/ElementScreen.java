package net.deechael.fabric.brightmagic.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
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

public class ElementScreen extends Screen {

    private static void doNothing(ButtonWidget button) {}

    private final Screen parent;

    private final List<TexturedButtonWidget> texturedButtonWidgets = new ArrayList<>();

    private final Map<TexturedButtonWidget, Element> elementMap = new HashMap<>();

    private ButtonWidget previousPage;
    private ButtonWidget nextPage;

    private int page = 1;

    private final List<Element> deny = new ArrayList<>();

    public ElementScreen(Screen parent) {
        this(parent, Text.translatable("brightmagic.gui.element.title"));
    }

    public ElementScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.texturedButtonWidgets.clear();
        this.elementMap.clear();
        this.deny.clear();

        int left = (this.width - (16 * 7)) / 2;
        int top = (this.height - (16 * 7)) / 2;

        Element[] elements = Element.getAll();
        int pages = elements.length % 12 == 0 ? elements.length / 12 : elements.length / 12 + 1;
        if (this.page < 1)
            this.page = 1;
        if (this.page > pages)
            this.page = pages;
        for (int i = (page - 1) * 12; i < Math.min(elements.length, pages * 12); i++) {
            int j = i - (page - 1) * 12;
            int x = left + ((j % 4) * 32);
            int y = top + ((j / 4) * 32);
            TexturedButtonWidget texturedButtonWidget = this.addDrawableChild(texture(elements[i], x, y));
            this.texturedButtonWidgets.add(texturedButtonWidget);
            this.elementMap.put(texturedButtonWidget, elements[i]);
        }
        this.previousPage = this.addDrawableChild(new ButtonWidget(left, top + 96, 16, 16, Text.literal("<"), button -> {
            this.page -= 1;
            this.clearAndInit();
        }));
        this.nextPage = this.addDrawableChild(new ButtonWidget(left + 96, top + 96, 16, 16, Text.literal(">"), button -> {
            this.page += 1;
            this.clearAndInit();
        }));
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    private TexturedButtonWidget texture(Element element, int x, int y) {
        return new TexturedButtonWidget(x, y, 16, 16, 0, 0, 16, element.getTexture(), 16, 16, (button -> {
            if (this.deny.contains(element))
                return;
            if (Skill.getSkills(element).isEmpty())
                return;
            this.client.setScreen(new SkillScreen(this, element));
        }));
    }

    public void close() {
        this.client.setScreen(this.parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        for (TexturedButtonWidget texturedButtonWidget : this.texturedButtonWidgets) {
            texturedButtonWidget.render(matrices, mouseX, mouseY, delta);
            Element element = this.elementMap.get(texturedButtonWidget);
            if (this.client == null)
                continue;
            if (!ElementData.isUnlocked((IDataHolder) this.client.player, element)) {
                this.deny.add(element);
                RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_DENY);
                DrawableHelper.drawTexture(matrices, texturedButtonWidget.x, texturedButtonWidget.y, 0, 0, 16, 16, 16, 16);
            }
        }
        this.previousPage.render(matrices, mouseX, mouseY, delta);
        this.nextPage.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
    }

}
