package net.deechael.fabric.brightmagic.util;

import net.deechael.fabric.brightmagic.util.functions.EightParametersFunction;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class ParticleUtils {

    public static void circle(World world, Vec3d position, double radius, int count, ParticleEffect particleEffect) {
        if (world.isClient)
            circleClient((ClientWorld) world, position, radius, particleEffect, false);
        else
            circleServer((ServerWorld) world, position, radius, count, particleEffect);
    }

    public static void circleImportant(World world, Vec3d position, double radius, int count, ParticleEffect particleEffect) {
        if (world.isClient)
            circleClient((ClientWorld) world, position, radius, particleEffect, true);
        else
            circleServer((ServerWorld) world, position, radius, count, particleEffect);
    }

    private static void circleServer(ServerWorld world, Vec3d position, double radius, int count, ParticleEffect particleEffect) {
        double x = position.x;
        double y = position.y;
        double z = position.z;
        for (int i = 0; i <= 90; i++) {
            double radians = Math.toRadians(i);
            double x0 = Math.cos(radians) * radius;
            double z0 = Math.sin(radians) * radius;
            world.spawnParticles(particleEffect, x + x0, y, z + z0, count, 0, 0, 0, 0);
            world.spawnParticles(particleEffect, x + x0, y, z - z0, count, 0, 0, 0, 0);
            world.spawnParticles(particleEffect, x - x0, y, z + z0, count, 0, 0, 0, 0);
            world.spawnParticles(particleEffect, x - x0, y, z - z0, count, 0, 0, 0, 0);
        }
    }

    private static void circleClient(ClientWorld world, Vec3d position, double radius, ParticleEffect particleEffect, boolean important) {
        EightParametersFunction<World, ParticleEffect, Double, Double, Double, Double, Double, Double>
                method = important ? World::addImportantParticle : World::addParticle;
        double x = position.x;
        double y = position.y;
        double z = position.z;
        for (int i = 0; i <= 90; i++) {
            double radians = Math.toRadians(i);
            double x0 = Math.cos(radians) * radius;
            double z0 = Math.sin(radians) * radius;
            method.apply(world, particleEffect, x + x0, y, z + z0, 0d, 0d, 0d);
            method.apply(world, particleEffect, x + x0, y, z - z0, 0d, 0d, 0d);
            method.apply(world, particleEffect, x - x0, y, z + z0, 0d, 0d, 0d);
            method.apply(world, particleEffect, x - x0, y, z - z0, 0d, 0d, 0d);
        }
    }

    private ParticleUtils() {}

}
