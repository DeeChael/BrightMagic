package net.deechael.fabric.brightmagic.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deechael.fabric.brightmagic.client.SkillCooldownHelper;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.item.UltimateWandItem;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.skill.SkillData;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.*;
import java.util.List;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin {

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Shadow public abstract TextRenderer getTextRenderer();

    private InGameHud brightmagic$asMinecraft() {
        return ((InGameHud) ((Object) this));
    }

    @ModifyArg(method = "renderHealthBar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawHeart(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIIZZ)V"),
            index = 3)
    private int renderHealth(int y) {
        // Move health bar up 6
        return y - 6;
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
            index = 2)
    private int renderExperienceBar$drawTexture(int y) {
        // Move exp bar up 6
        return y - 6;
    }

    @ModifyArg(method = "renderMountHealth", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
            index = 2)
    private int renderMountHealth(int y) {
        if (this.getCameraPlayer().isCreative())
            return y;
        return y - 6;
    }

    @ModifyArg(method = "renderMountJumpBar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"),
            index = 2)
    private int renderMountJumpBar$draw(int y) {
        if (this.getCameraPlayer().isCreative())
            return y;
        return y - 6;
    }

    @ModifyVariable(method = "renderHeldItemTooltip", at = @At(value = "STORE"), ordinal = 2)
    private int modifyHeldItemTooltip(int y) {
        return y - 6;
    }

    @ModifyVariable(method = "renderStatusBars", at = @At(value = "STORE"), ordinal = 5)
    private int modifyStatusBars(int value) {
        return value - 6;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V", ordinal = 0), index = 1)
    private double modifyActionbar(double value) {
        return value - 6;
    }


    @Inject(method = "renderMountJumpBar", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderMountJumpBar(MatrixStack matrices, int x, CallbackInfo ci) {
        if (this.getCameraPlayer().isCreative())
            return;
        // Rendering mana bar on mounting
        IDataHolder dataHolder = (IDataHolder) this.getCameraPlayer();
        int currentMana = ManaData.getMana(dataHolder);
        int maxMana = ManaData.getMaxMana(dataHolder);
        RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_ICONS);
        int l = this.scaledHeight - 32 + 3;
        this.brightmagic$asMinecraft().drawTexture(matrices, x, l, 0, 0, 182, 5);
        this.brightmagic$asMinecraft().drawTexture(matrices, x, l, 0, 5, (int) Math.max(0, Math.min(183F * ((float) currentMana / (float) maxMana), 183F)), 5);
        String string = "" + currentMana;
        int k = x + 182 + 2;
        l = this.scaledHeight - 31 + 1;
        this.getTextRenderer().draw(matrices, string, (float) (k + 1), (float) l, 0);
        this.getTextRenderer().draw(matrices, string, (float) (k - 1), (float) l, 0);
        this.getTextRenderer().draw(matrices, string, (float) k, (float) (l + 1), 0);
        this.getTextRenderer().draw(matrices, string, (float) k, (float) (l - 1), 0);
        this.getTextRenderer().draw(matrices, string, (float) k, (float) l, ((0xFF) << 24) | ((116 & 0xFF) << 16) | ((158 & 0xFF) << 8)  | ((244 & 0xFF)));
    }

    @ModifyArg(method = "renderExperienceBar", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Ljava/lang/String;FFI)I"),
            index = 3)
    private float renderExperienceBar$draw(float y) {
        // Move exp bar level text up 6
        return y - 6;
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"))
    private void renderExperienceBar(MatrixStack matrices, int x, CallbackInfo ci) {
        // Rendering mana bar
        IDataHolder dataHolder = (IDataHolder) this.getCameraPlayer();
        int currentMana = ManaData.getMana(dataHolder);
        int maxMana = ManaData.getMaxMana(dataHolder);
        RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_ICONS);
        int l = this.scaledHeight - 32 + 3;
        this.brightmagic$asMinecraft().drawTexture(matrices, x, l, 0, 0, 182, 5);
        this.brightmagic$asMinecraft().drawTexture(matrices, x, l, 0, 5, (int) Math.max(0, Math.min(183F * ((float) currentMana / (float) maxMana), 183F)), 5);
        String string = "" + currentMana;
        int k = x + 182 + 2;
        l = this.scaledHeight - 31 + 1;
        this.getTextRenderer().draw(matrices, string, (float) (k + 1), (float) l, 0);
        this.getTextRenderer().draw(matrices, string, (float) (k - 1), (float) l, 0);
        this.getTextRenderer().draw(matrices, string, (float) k, (float) (l + 1), 0);
        this.getTextRenderer().draw(matrices, string, (float) k, (float) (l - 1), 0);
        this.getTextRenderer().draw(matrices, string, (float) k, (float) l, ((0xFF) << 24) | ((116 & 0xFF) << 16) | ((158 & 0xFF) << 8)  | ((244 & 0xFF)));
    }

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void renderHotbar$head(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        if (!(this.getCameraPlayer().getStackInHand(Hand.MAIN_HAND).getItem() instanceof UltimateWandItem))
            return;
        RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_ICONS);
        int x = this.scaledWidth - 95 - 2;
        int y = this.scaledHeight - 26 - 2;
        this.brightmagic$asMinecraft().drawTexture(matrices, x, y, 0, 10, 95, 26);

        IDataHolder dataHolder = (IDataHolder) this.getCameraPlayer();
        Skill skill1 = SkillData.getSlot(dataHolder, 1);
        Skill skill2 = SkillData.getSlot(dataHolder, 2);
        Skill skill3 = SkillData.getSlot(dataHolder, 3);
        Skill skill4 = SkillData.getSlot(dataHolder, 4);
        if (skill1 != null) {
            RenderSystem.setShaderTexture(0, skill1.getTexture());
            DrawableHelper.drawTexture(matrices, x + 5, y + 5, 0, 0, 16, 16, 16, 16);
            int cd = SkillCooldownHelper.getCd(skill1);
            if (cd != 0) {
                this.getTextRenderer().draw(matrices, Text.literal("" + cd), x + 5 + 2, y + 5 + 2, Color.WHITE.getRGB());
            }
            if (skill1.getElement() != null) {
                if (!ElementData.isUnlocked((IDataHolder) this.getCameraPlayer(), skill1.getElement())) {
                    RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_DENY);
                    DrawableHelper.drawTexture(matrices, x + 78 + 5, y + 5, 0, 0, 16, 16, 16, 16);
                }
            }
        }
        if (skill2 != null) {
            RenderSystem.setShaderTexture(0, skill2.getTexture());
            DrawableHelper.drawTexture(matrices, x + 26 + 5, y + 5, 0, 0, 16, 16, 16, 16);
            int cd = SkillCooldownHelper.getCd(skill2);
            if (cd != 0) {
                this.getTextRenderer().draw(matrices, Text.literal("" + cd), x + 26 + 5 + 2, y + 5 + 2, Color.WHITE.getRGB());
            }
            if (skill2.getElement() != null) {
                if (!ElementData.isUnlocked((IDataHolder) this.getCameraPlayer(), skill2.getElement())) {
                    RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_DENY);
                    DrawableHelper.drawTexture(matrices, x + 78 + 5, y + 5, 0, 0, 16, 16, 16, 16);
                }
            }
        }
        if (skill3 != null) {
            RenderSystem.setShaderTexture(0, skill3.getTexture());
            DrawableHelper.drawTexture(matrices, x + 52 + 5, y + 5, 0, 0, 16, 16, 16, 16);
            int cd = SkillCooldownHelper.getCd(skill3);
            if (cd != 0) {
                this.getTextRenderer().draw(matrices, Text.literal("" + cd), x + 52 + 5 + 2, y + 5 + 2, Color.WHITE.getRGB());
            }
            if (skill3.getElement() != null) {
                if (!ElementData.isUnlocked((IDataHolder) this.getCameraPlayer(), skill3.getElement())) {
                    RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_DENY);
                    DrawableHelper.drawTexture(matrices, x + 78 + 5, y + 5, 0, 0, 16, 16, 16, 16);
                }
            }
        }
        if (skill4 != null) {
            RenderSystem.setShaderTexture(0, skill4.getTexture());
            DrawableHelper.drawTexture(matrices, x + 78 + 5, y + 5, 0, 0, 16, 16, 16, 16);
            int cd = SkillCooldownHelper.getCd(skill4);
            if (cd != 0) {
                this.getTextRenderer().draw(matrices, Text.literal("" + cd), x + 78 + 5 + 2, y + 5 + 2, Color.WHITE.getRGB());
            }
            if (skill4.getElement() != null) {
                if (!ElementData.isUnlocked((IDataHolder) this.getCameraPlayer(), skill4.getElement())) {
                    RenderSystem.setShaderTexture(0, BrightMagicTextures.GUI_DENY);
                    DrawableHelper.drawTexture(matrices, x + 78 + 5, y + 5, 0, 0, 16, 16, 16, 16);
                }
            }
        }
    }

    @Inject(method = "renderHotbar", at = @At("TAIL"))
    private void renderHotbar$tail(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        List<Element> onBody = ((ElementContainer) this.getCameraPlayer()).owningElement();
        int top = 48;
        int left = this.scaledWidth - 16 ;
        int lines = onBody.size() % 6 == 0 ? onBody.size() / 6 : onBody.size() / 6 + 1;
        if (lines == 1) {
            for (int i = 0; i < onBody.size(); i++) {
                RenderSystem.setShaderTexture(0, onBody.get(i).getTexture());
                DrawableHelper.drawTexture(matrices, left - (16 * i), top, 0, 0, 16, 16, 16, 16);
            }
        } else {
            int lastLineNumber = onBody.size() % 6;
            if (lastLineNumber == 0) {
                for (int j = 0; j < lines; j++) {
                    for (int i = 0; i < onBody.size(); i++) {
                        RenderSystem.setShaderTexture(0, onBody.get((j * 6) +i).getTexture());
                        DrawableHelper.drawTexture(matrices, left - (16 * i), top + (16 * j), 0, 0, 16, 16, 16, 16);
                    }
                }
            } else {
                for (int j = 0; j < lines; j++) {
                    for (int i = 0; i < onBody.size(); i++) {
                        RenderSystem.setShaderTexture(0, onBody.get((j * 6) +i).getTexture());
                        DrawableHelper.drawTexture(matrices, left - (16 * i), top + (16 * j), 0, 0, 16, 16, 16, 16);
                    }
                }
                for (int i = 0; i < lastLineNumber; i++) {
                    RenderSystem.setShaderTexture(0, onBody.get(((lines - 1) * 6) +i).getTexture());
                    DrawableHelper.drawTexture(matrices, left - (16 * i), top + (16 * (lines - 1)), 0, 0, 16, 16, 16, 16);
                }
            }
        }
    }

}
