package net.deechael.fabric.brightmagic.element.reaction.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.Element;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.element.reaction.ElementReaction;
import net.deechael.fabric.brightmagic.skill.DamageableSkill;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class OverloadedReaction extends ElementReaction {

    public OverloadedReaction() {
        super(Identifier.of(Constants.MOD_ID, "overloaded"), ElementType.PYRO, ElementType.ELECTRO);
    }

    @Override
    public void react(World world, LivingEntity entity, Element first, Element second, ItemStack itemStack, Skill skill) {
        if (skill instanceof DamageableSkill damageableSkill) {
            double damage = damageableSkill.getDamage(world, entity);
            damageableSkill.use(world, entity, itemStack, damage * 2d);
        }
        Vec3d pos = entity.getPos();
        world.createExplosion(entity, ElementReaction.ELEMENT_REACTION_DAMAGE_SOURCE, new ExplosionBehavior() {
            @Override
            public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
                return false;
            }
        }, pos.x, pos.y, pos.z, 1.0f, true, Explosion.DestructionType.NONE);
    }

    @Override
    public void react(World world, LivingEntity entity, Element first, Element second) {
        Vec3d pos = entity.getPos();
        world.createExplosion(entity, pos.x, pos.y, pos.z, 1.0f, true, Explosion.DestructionType.NONE);
    }

}
