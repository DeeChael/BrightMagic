package net.deechael.fabric.brightmagic.basic;

import net.deechael.fabric.brightmagic.element.ElementData;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.mana.ManaData;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.util.IDataHolder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class BasicWandItem extends RangedWeaponItem {

    private final int range;
    private final int baseManaCost;
    private final int cooldown;

    private final Element[] allowedElements;

    private final Skill limitedSkill;

    public BasicWandItem(Settings settings,
                         Element[] allowedElements,
                         Skill limitedSkill,
                         int baseManaCost,
                         int cooldown,
                         int range) {
        super(settings);
        this.allowedElements = allowedElements;
        this.limitedSkill = limitedSkill;
        this.baseManaCost = baseManaCost;
        this.cooldown = cooldown;
        this.range = range;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (this.limitedSkill == null)
            return TypedActionResult.pass(user.getStackInHand(hand));
        if (world.isClient) {
            if (!user.isCreative()) {
                if (user.getStackInHand(hand).getItem() != this)
                    return TypedActionResult.pass(user.getStackInHand(hand));
                if (ManaData.getMana((IDataHolder) user) < this.baseManaCost)
                    return TypedActionResult.pass(user.getStackInHand(hand));
                if (this.limitedSkill.getElement() != null && !ElementData.hasUnlocked((IDataHolder) user, this.limitedSkill.getElement()))
                    return TypedActionResult.pass(user.getStackInHand(hand));
            }
            this.limitedSkill.render(user, world, user.getStackInHand(hand));
            return TypedActionResult.pass(user.getStackInHand(hand));
        } else {
            if (!user.isCreative()) {
                if (user.getStackInHand(hand).getItem() != this)
                    return TypedActionResult.pass(user.getStackInHand(hand));
                if (ManaData.getMana((IDataHolder) user) < this.baseManaCost) {
                    user.sendMessage(Text.translatable("brightmagic.messages.mananotenough").formatted(Formatting.RED));
                    return TypedActionResult.fail(user.getStackInHand(hand));
                }
                if (this.limitedSkill.getElement() != null && !ElementData.hasUnlocked((IDataHolder) user, this.limitedSkill.getElement())) {
                    user.sendMessage(Text.translatable("brightmagic.messages.noelement").formatted(Formatting.RED));
                    return TypedActionResult.fail(user.getStackInHand(hand));
                }
                ManaData.removeMana((IDataHolder) user, baseManaCost);
            }
        }
        user.getItemCooldownManager().set(this, this.cooldown * 20);
        this.limitedSkill.use(user, world, user.getStackInHand(hand));
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    public Element[] getAllowedElements() {
        return allowedElements;
    }

    @Override
    public int getRange() {
        return this.range;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (this.limitedSkill == null)
            return;
        MutableText skillText = Text
                .translatable("brightmagic.lore.wandskill")
                .formatted(Formatting.YELLOW);
        Text skillName = Text
                .translatable(this.limitedSkill.getTranslateKey())
                .formatted(Formatting.GOLD);
        skillText.append(skillName);
        tooltip.add(skillText);
        if (this.limitedSkill.getElement() != null) {
            MutableText base = Text.translatable("brightmagic.lore.wandelement").formatted(Formatting.YELLOW);
            base.append(Text.translatable("magic.element." + this.limitedSkill.getElement().getId().getNamespace() + "." + this.limitedSkill.getElement().getId().getPath()).formatted(this.limitedSkill.getElement().getColor()));
            tooltip.add(base);
        }
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        // if (this.limitedSkill != null) {
        //     return Optional.of(new SkillTooltipData(this.limitedSkill));
        // }
        return super.getTooltipData(stack);
    }

}
