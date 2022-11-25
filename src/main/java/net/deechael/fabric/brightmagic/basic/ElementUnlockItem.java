package net.deechael.fabric.brightmagic.basic;

import com.mojang.blaze3d.systems.RenderSystem;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElementUnlockItem extends Item {

    private final Element element;

    public ElementUnlockItem(Settings settings, @NotNull Element element) {
        super(settings);
        this.element = element;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        MutableText base = Text.translatable("brightmagic.lore.elementunlocker").formatted(Formatting.YELLOW);
        base.append(Text.translatable("magic.element." + this.element.getId().getNamespace() + "." + this.element.getId().getPath()).formatted(this.element.getColor()));
        tooltip.add(base);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient)
            return TypedActionResult.pass(user.getStackInHand(hand));
        if (ElementData.getUnlocked((IDataHolder) user) >= 4) {
            user.sendMessage(Text.translatable("brightmagic.messages.elementmaxed").formatted(Formatting.RED));
            return TypedActionResult.fail(user.getStackInHand(hand));
        } else if (ElementData.isUnlocked((IDataHolder) user, this.element)) {
            user.sendMessage(Text.translatable("brightmagic.messages.elementunlocked").formatted(Formatting.RED));
            return TypedActionResult.fail(user.getStackInHand(hand));
        } else {
            ElementData.addUnlocked((IDataHolder) user, this.element);
            user.sendMessage(Text.translatable("brightmagic.messages.unlockedelement").formatted(Formatting.YELLOW)
                    .append(Text.translatable("magic.element." + this.element.getId().getNamespace() + "." + this.element.getId().getPath()).formatted(this.element.getColor())));
            return TypedActionResult.success(user.getStackInHand(hand));
        }
    }

}
