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

public class VaporizeReaction extends ElementReaction {

    public VaporizeReaction() {
        super(Identifier.of(Constants.MOD_ID, "vaporize"), ElementType.PYRO, ElementType.HYDRO);
    }

    @Override
    public void react(World world, LivingEntity entity, Element first, Element second, ItemStack itemStack, Skill skill) {
        if (skill instanceof DamageableSkill damageableSkill) {
            double damage = damageableSkill.getDamage(world, entity);
            damageableSkill.use(world, entity, itemStack, damage * 1.5d);
        }
    }

    @Override
    public void react(World world, LivingEntity entity, Element first, Element second) {

    }

}
