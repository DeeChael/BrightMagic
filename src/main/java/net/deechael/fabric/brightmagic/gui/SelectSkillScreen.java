package net.deechael.fabric.brightmagic.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deechael.fabric.brightmagic.networking.packet.SkillC2SPacket;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class SelectSkillScreen extends Screen {

    private static void doNothing(ButtonWidget button) {}

    private final Screen parent;

    private ButtonWidget backButton;

    private TexturedButtonWidget slot1;
    private TexturedButtonWidget slot2;
    private TexturedButtonWidget slot3;
    private TexturedButtonWidget slot4;

    public SelectSkillScreen(Screen parent) {
        this(parent, Text.translatable("brightmagic.gui.element.title"));
    }

    public SelectSkillScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        int left = (this.width - (26 * 7)) / 2;
        int top = (this.height - 26) / 2;

        IDataHolder dataHolder = ((IDataHolder) this.client.player);
        Skill skill1 = SkillData.getSlot(dataHolder, 1);
        Skill skill2 = SkillData.getSlot(dataHolder, 2);
        Skill skill3 = SkillData.getSlot(dataHolder, 3);
        Skill skill4 = SkillData.getSlot(dataHolder, 4);
        this.slot1 = skill1 == null ? texture(left + 5, top + 5, 1) : texture(skill1, left + 5, top + 5, 1);
        this.slot2 = skill2 == null ? texture(left + 52 + 5, top + 5, 2) : texture(skill2, left + 52 + 5, top + 5, 2);
        this.slot3 = skill3 == null ? texture(left + 104 + 5, top + 5, 3) : texture(skill3, left + 104 + 5, top + 5, 3);
        this.slot4 = skill4 == null ? texture(left + 156 + 5, top + 5, 4) : texture(skill4, left + 156 + 5, top + 5, 4);
        this.backButton = this.addDrawableChild(new ButtonWidget(16, 16, 16, 16, Text.literal("<"), button -> {
            this.close();
        }));
    }

    private TexturedButtonWidget texture(Skill skill, int x, int y, int slot) {
        return new TexturedButtonWidget(x, y, 16, 16, 0, 0, 16, skill.getTexture(), 16, 16, (button -> {
            if (!hasShiftDown()) {
                this.client.setScreen(new UnlockedSkillScreen(this, slot));
            } else {
                SkillC2SPacket.writeC2SSetSlotSkillPacket(this.client.player, slot, null);
                this.clearAndInit();
            }
        }));
    }

    private TexturedButtonWidget texture(int x, int y, int slot) {
        return new TexturedButtonWidget(x, y, 16, 16, 0, 0, 16, BrightMagicTextures.GUI_DENY, 16, 16, (button -> this.client.setScreen(new UnlockedSkillScreen(this, slot))));
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
        {
            // Draw the border of the skills

            int left = (this.width - (26 * 7)) / 2;
            int top = (this.height - 26) / 2;

            RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_ICONS);
            this.drawTexture(matrices, left, top, 0, 36, 26, 26);
            this.drawTexture(matrices, left + 52, top, 0, 36, 26, 26);
            this.drawTexture(matrices, left + 104, top, 0, 36, 26, 26);
            this.drawTexture(matrices, left + 156, top, 0, 36, 26, 26);
        }
        slot1.render(matrices, mouseX, mouseY, delta);
        slot2.render(matrices, mouseX, mouseY, delta);
        slot3.render(matrices, mouseX, mouseY, delta);
        slot4.render(matrices, mouseX, mouseY, delta);
        this.backButton.render(matrices, mouseX, mouseY, delta);
    }

}
