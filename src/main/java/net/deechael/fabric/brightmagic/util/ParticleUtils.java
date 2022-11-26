package net.deechael.fabric.brightmagic.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ParticleUtils {

    public static void circle(World world, Vec3d position, double radius, ParticleEffect particleEffect) {
        double x = position.x;
        double y = position.y;
        double z = position.z;
        for (int i = 0; i <= 90; i++) {
            double radians = Math.toRadians(i);
            double x0 = Math.cos(radians) * radius;
            double z0 = Math.sin(radians) * radius;
            world.addParticle(particleEffect, x + x0, y, z + z0, 0, 0, 0);
            world.addParticle(particleEffect, x + x0, y, z - z0, 0, 0, 0);
            world.addParticle(particleEffect, x - x0, y, z + z0, 0, 0, 0);
            world.addParticle(particleEffect, x - x0, y, z - z0, 0, 0, 0);
        }
    }

    public static void circleImportant(World world, Vec3d position, double radius, ParticleEffect particleEffect) {
        double x = position.x;
        double y = position.y;
        double z = position.z;
        for (int i = 0; i <= 90; i++) {
            double radians = Math.toRadians(i);
            double x0 = Math.cos(radians) * radius;
            double z0 = Math.sin(radians) * radius;
            world.addImportantParticle(particleEffect, x + x0, y, z + z0, 0, 0, 0);
            world.addImportantParticle(particleEffect, x + x0, y, z - z0, 0, 0, 0);
            world.addImportantParticle(particleEffect, x - x0, y, z + z0, 0, 0, 0);
            world.addImportantParticle(particleEffect, x - x0, y, z - z0, 0, 0, 0);
        }
    }

    private ParticleUtils() {}

}
