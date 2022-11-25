package net.deechael.fabric.brightmagic.element.reaction.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.skill.DamageableSkill;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class OverloadedReaction extends ElementReaction {

    public OverloadedReaction() {
        super(Identifier.of(Constants.MOD_ID, "overloaded"), ElementType.PYRO, ElementType.ELECTRO);
    }

    @Override
    public void react(World world, Entity entity, ItemStack itemStack, Skill skill) {
        if (skill instanceof DamageableSkill damageableSkill) {
            double damage = damageableSkill.getDamage(world, entity);
            damageableSkill.use(world, entity, itemStack, damage * 2d);
        } else {
            entity.damage(ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, 10);
        }
    }

    @Override
    public void react(World world, Entity entity) {
        entity.damage(ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, 10);
    }

}
