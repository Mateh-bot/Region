package org.mateh.region.utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.mateh.region.Main;
import org.mateh.region.models.Region;

public class RegionUtils {

    public static Region getRegionAtLocation(Location loc) {
        for (Region region : Main.getInstance().getRegionManager().getRegions().values()) {
            if (region.isInside(loc)) {
                return region;
            }
        }
        return null;
    }

    public static void displayFullRegionParticles(Region region) {
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

        java.util.function.BiConsumer<Location, Location> displayLine = (start, end) -> {
            double distance = start.distance(end);
            int steps = (int) (distance / increment);
            if (steps == 0) steps = 1;
            for (int i = 0; i <= steps; i++) {
                double t = (double) i / steps;
                double x = start.getX() + (end.getX() - start.getX()) * t;
                double y = start.getY() + (end.getY() - start.getY()) * t;
                double z = start.getZ() + (end.getZ() - start.getZ()) * t;
                Location point = new Location(world, x, y, z);
                world.spawnParticle(Particle.HAPPY_VILLAGER, point, 1);
            }
        };

        // Bottom edges
        displayLine.accept(new Location(world, minX, minY, minZ), new Location(world, maxX, minY, minZ));
        displayLine.accept(new Location(world, minX, minY, maxZ), new Location(world, maxX, minY, maxZ));
        displayLine.accept(new Location(world, minX, minY, minZ), new Location(world, minX, minY, maxZ));
        displayLine.accept(new Location(world, maxX, minY, minZ), new Location(world, maxX, minY, maxZ));

        // Top edges
        displayLine.accept(new Location(world, minX, maxY, minZ), new Location(world, maxX, maxY, minZ));
        displayLine.accept(new Location(world, minX, maxY, maxZ), new Location(world, maxX, maxY, maxZ));
        displayLine.accept(new Location(world, minX, maxY, minZ), new Location(world, minX, maxY, maxZ));
        displayLine.accept(new Location(world, maxX, maxY, minZ), new Location(world, maxX, maxY, maxZ));

        // Vertical edges
        displayLine.accept(new Location(world, minX, minY, minZ), new Location(world, minX, maxY, minZ));
        displayLine.accept(new Location(world, maxX, minY, minZ), new Location(world, maxX, maxY, minZ));
        displayLine.accept(new Location(world, minX, minY, maxZ), new Location(world, minX, maxY, maxZ));
        displayLine.accept(new Location(world, maxX, minY, maxZ), new Location(world, maxX, maxY, maxZ));
    }
}
