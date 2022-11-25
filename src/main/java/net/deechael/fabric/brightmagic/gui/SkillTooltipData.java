package net.deechael.fabric.brightmagic.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class SkillTooltipData implements TooltipData, TooltipComponent {

    private final Skill skill;

    public SkillTooltipData(Skill skill) {
        this.skill = skill;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 16;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer, int z) {
        RenderSystem.setShaderTexture(0, this.skill.getTexture());
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, 16, 16, 16, 16);
    }

    public TooltipData getComponent() {
        return this;
    }

}
