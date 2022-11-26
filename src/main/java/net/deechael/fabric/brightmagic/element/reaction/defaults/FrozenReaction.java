package net.deechael.fabric.brightmagic.element.reaction.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FrozenReaction extends ElementReaction {

    public FrozenReaction() {
        super(Identifier.of(Constants.MOD_ID, "frozen"), ElementType.HYDRO, ElementType.CRYO);
    }

    @Override
    public void react(World world, LivingEntity entity, ItemStack itemStack, Skill skill) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5 * 20, 255));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 5 * 20, 255));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5 * 20, -128));
    }

    @Override
    public void react(World world, LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5 * 20, 255));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 5 * 20, 255));
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 5 * 20, -128));
    }

}
