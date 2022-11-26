package net.deechael.fabric.brightmagic.element.reaction;

import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class ElementReaction {

    public final static DamageSource ELEMENT_REACTION_DAMAGE_SOURCE = new DamageSource("element_reaction");

    private final Identifier id;
    private final Element first;
    private final Element second;

    public ElementReaction(Identifier id, Element first, Element second) {
        this.id = id;
        this.first = first;
        this.second = second;
    }

    public Identifier getId() {
        return id;
    }

    public Element getFirst() {
        return first;
    }

    public Element getSecond() {
        return second;
    }

    public abstract void react(World world, LivingEntity entity, ItemStack itemStack, Skill skill);

    public abstract void react(World world, LivingEntity entity);

}
