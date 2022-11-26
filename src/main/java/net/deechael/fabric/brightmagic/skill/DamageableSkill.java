package net.deechael.fabric.brightmagic.skill;

import net.deechael.fabric.brightmagic.element.Element;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class DamageableSkill extends Skill {

    public DamageableSkill(Identifier identifier, Element element, int manaCost, Identifier texture) {
        super(identifier, element, manaCost, texture);
    }

    public abstract double getDamage(World world, Entity user);

    public void use(World world, Entity entity, ItemStack wand, double damage) {

    }

}
