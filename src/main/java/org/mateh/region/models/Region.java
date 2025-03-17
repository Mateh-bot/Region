package org.mateh.region.models;

import org.bukkit.Location;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Region {
    private final String name;
    private Location loc1, loc2;
    private final Set<String> whitelist;
    private final Map<RegionFlag, FlagState> flags;
    private boolean showParticles = false;

    public Region(String name, Location loc1, Location loc2) {
        this.name = name;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.whitelist = new HashSet<>();
        this.flags = new HashMap<>();
        for (RegionFlag flag : RegionFlag.values()) {
            flags.put(flag, FlagState.EVERYONE);
        }
    }

    public String getName() {
        return name;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }

    public Set<String> getWhitelist() {
        return whitelist;
    }

    public void addWhitelist(String username) {
        whitelist.add(username.toLowerCase());
    }

    public void removeWhitelist(String username) {
        whitelist.remove(username.toLowerCase());
    }

    public Map<RegionFlag, FlagState> getFlags() {
        return flags;
    }

    public void setFlag(RegionFlag flag, FlagState state) {
        flags.put(flag, state);
    }

    public FlagState getFlagState(RegionFlag flag) {
        return flags.get(flag);
    }

    public boolean isInside(Location loc) {
        if (!loc.getWorld().equals(loc1.getWorld())) return false;
        double minX = Math.min(loc1.getX(), loc2.getX());
        double maxX = Math.max(loc1.getX(), loc2.getX()) + 1;
        double minY = Math.min(loc1.getY(), loc2.getY());
        double maxY = Math.max(loc1.getY(), loc2.getY()) + 1;
        double minZ = Math.min(loc1.getZ(), loc2.getZ());
        double maxZ = Math.max(loc1.getZ(), loc2.getZ()) + 1;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        return x >= minX && x < maxX &&
                y >= minY && y < maxY &&
                z >= minZ && z < maxZ;
    }

    public boolean isShowingParticles() {
        return showParticles;
    }

    public void setShowParticles(boolean showParticles) {
        this.showParticles = showParticles;
    }
}
