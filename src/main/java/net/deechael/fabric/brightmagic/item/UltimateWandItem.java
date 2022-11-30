package net.deechael.fabric.brightmagic.item;

import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.registry.BrightMagicItems;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class UltimateWandItem extends RangedWeaponItem {

    public UltimateWandItem(Settings settings) {
        super(settings);
    }

    public void use(World world, LivingEntity entity, ItemStack stack, Skill skill) {
        if (entity instanceof PlayerEntity player && !player.isCreative()) {
            if (player.getStackInHand(Hand.MAIN_HAND).getItem() != BrightMagicItems.FINAL_WAND)
                return;
            if (ManaData.getMana((IDataHolder) player) < skill.getManaCost()) {
                player.sendMessage(Text.translatable("brightmagic.messages.mananotenough").formatted(Formatting.RED));
                return;
            }
            if (skill.getElement() != null && !ElementData.hasUnlocked((IDataHolder) player, skill.getElement())) {
                player.sendMessage(Text.translatable("brightmagic.messages.noelement").formatted(Formatting.RED));
                return;
            }
            ManaData.removeMana((IDataHolder) player, skill.getManaCost());
        }
        skill.use(entity, world, stack);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    @Override
    public int getRange() {
        return -1;
    }

}
