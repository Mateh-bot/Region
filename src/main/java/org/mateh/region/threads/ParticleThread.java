package org.mateh.region.threads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.mateh.region.Main;
import org.mateh.region.models.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ParticleThread {

    public static void drawRegionParticles(Region region) {
        Location loc1 = region.getLoc1();
        Location loc2 = region.getLoc2();
        if (!loc1.getWorld().equals(loc2.getWorld())) return;

        World world = loc1.getWorld();
        double minX = Math.min(loc1.getX(), loc2.getX());
        double maxX = Math.max(loc1.getX(), loc2.getX()) + 1;
        double minY = Math.min(loc1.getY(), loc2.getY());
        double maxY = Math.max(loc1.getY(), loc2.getY()) + 1;
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ()) + 1;
        double increment = 0.5;

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Bottom edges
        futures.add(spawnLine(world, new Location(world, minX, minY, minZ), new Location(world, maxX, minY, minZ), increment));
        futures.add(spawnLine(world, new Location(world, minX, minY, maxZ), new Location(world, maxX, minY, maxZ), increment));
        futures.add(spawnLine(world, new Location(world, minX, minY, minZ), new Location(world, minX, minY, maxZ), increment));
        futures.add(spawnLine(world, new Location(world, maxX, minY, minZ), new Location(world, maxX, minY, maxZ), increment));

        // Top edges
        futures.add(spawnLine(world, new Location(world, minX, maxY, minZ), new Location(world, maxX, maxY, minZ), increment));
        futures.add(spawnLine(world, new Location(world, minX, maxY, maxZ), new Location(world, maxX, maxY, maxZ), increment));
        futures.add(spawnLine(world, new Location(world, minX, maxY, minZ), new Location(world, minX, maxY, maxZ), increment));
        futures.add(spawnLine(world, new Location(world, maxX, maxY, minZ), new Location(world, maxX, maxY, maxZ), increment));

        // Vertical edges
        futures.add(spawnLine(world, new Location(world, minX, minY, minZ), new Location(world, minX, maxY, minZ), increment));
        futures.add(spawnLine(world, new Location(world, maxX, minY, minZ), new Location(world, maxX, maxY, minZ), increment));
        futures.add(spawnLine(world, new Location(world, minX, minY, maxZ), new Location(world, minX, maxY, maxZ), increment));
        futures.add(spawnLine(world, new Location(world, maxX, minY, maxZ), new Location(world, maxX, maxY, maxZ), increment));

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private static CompletableFuture<Void> spawnLine(World world, Location start, Location end, double increment) {
        return CompletableFuture.runAsync(() -> {
            List<Location> positions = computeLine(start, end, increment);
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                for (Location pos : positions) {
                    world.spawnParticle(Particle.HAPPY_VILLAGER, pos, 1);
                }
            });
        });
    }

    private static List<Location> computeLine(Location start, Location end, double increment) {
        List<Location> positions = new ArrayList<>();
        double distance = start.distance(end);
        int steps = (int) (distance / increment);
        if (steps == 0) steps = 1;
        for (int i = 0; i <= steps; i++) {
            double t = (double) i / steps;
            double x = start.getX() + (end.getX() - start.getX()) * t;
            double y = start.getY() + (end.getY() - start.getY()) * t;
            double z = start.getZ() + (end.getZ() - start.getZ()) * t;
            positions.add(new Location(start.getWorld(), x, y, z));
        }
        return positions;
    }
}
