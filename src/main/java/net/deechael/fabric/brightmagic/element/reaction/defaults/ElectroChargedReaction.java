package net.deechael.fabric.brightmagic.element.reaction.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.skill.DamageableSkill;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ElectroChargedReaction extends ElementReaction {

    public ElectroChargedReaction() {
        super(Identifier.of(Constants.MOD_ID, "electro-charged"), ElementType.ELECTRO, ElementType.HYDRO);
    }

    @Override
    public void react(World world, LivingEntity entity, Element first, Element second, ItemStack itemStack, Skill skill) {
        if (skill instanceof DamageableSkill damageableSkill) {
            double damage = damageableSkill.getDamage(world, entity);
            damageableSkill.use(world, entity, itemStack, damage * 1.5d);
        } else {
            entity.damage(ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, 4);
        }
    }

    @Override
    public void react(World world, LivingEntity entity, Element first, Element second) {
        entity.damage(ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, 4);
    }

}
