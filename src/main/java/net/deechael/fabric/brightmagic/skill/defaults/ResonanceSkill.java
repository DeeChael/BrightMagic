package net.deechael.fabric.brightmagic.skill.defaults;

import net.deechael.fabric.brightmagic.Constants;
import net.deechael.fabric.brightmagic.element.ElementType;
import net.deechael.fabric.brightmagic.registry.client.BrightMagicTextures;
import net.deechael.fabric.brightmagic.skill.Skill;
import net.deechael.fabric.brightmagic.util.ElementContainer;
import net.deechael.fabric.brightmagic.util.ParticleUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ResonanceSkill extends Skill {

    public ResonanceSkill() {
        super(new Identifier(Constants.MOD_ID, "hydro_resonance"), ElementType.HYDRO, 20, BrightMagicTextures.SKILL_HYDRO_RESONANCE);
    }

    @Override
    public void use(PlayerEntity entity, World world, ItemStack wand) {
        Vec3d entityPos = entity.getPos();
        List<PlayerEntity> closed = new ArrayList<>();
        for (PlayerEntity player : world.getPlayers()) {
            if (player.getPos().distanceTo(entityPos) < 5)
                closed.add(player);
        }
        closed.forEach(player -> {
            player.heal(4f);
            ((ElementContainer) entity).addElement(ResonanceSkill.this.getElement(), wand, ResonanceSkill.this);
            ParticleUtils.circleImportant(world, entityPos, 5, 10, ParticleTypes.DRIPPING_WATER);
            ParticleUtils.circleImportant(world, entityPos, 4.5, 10, ParticleTypes.DRIPPING_WATER);
        });
    }

    @Override
    public void render(PlayerEntity entity, World world, ItemStack hand) {
    }

}
