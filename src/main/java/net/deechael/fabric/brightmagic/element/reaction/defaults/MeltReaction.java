package net.deechael.fabric.brightmagic.element.reaction.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.skill.DamageableSkill;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MeltReaction extends ElementReaction {

    public MeltReaction() {
        super(Identifier.of(Constants.MOD_ID, "melt"), ElementType.PYRO, ElementType.CRYO);
    }

    @Override
    public void react(World world, LivingEntity entity, ItemStack itemStack, Skill skill) {
        if (skill instanceof DamageableSkill damageableSkill) {
            double damage = damageableSkill.getDamage(world, entity);
            damageableSkill.use(world, entity, itemStack, damage * 2d);
        } else {
            entity.damage(ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, 10);
        }
    }

    @Override
    public void react(World world, LivingEntity entity) {
        entity.damage(ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, 2);
    }

}
