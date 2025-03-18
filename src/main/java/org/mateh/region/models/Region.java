package org.mateh.region.models;

import org.bukkit.Location;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;

import java.util.HashMap;
import java.util.Map;

public class Region {
    private final String id;
    private String name;
    private final String owner;
    private Location loc1, loc2;
    private final Map<String, String> whitelist;
    private final Map<RegionFlag, FlagState> flags;
    private boolean showParticles = false;

    public Region(String id, String name, String owner, Location loc1, Location loc2) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.whitelist = new HashMap<>();
        this.flags = new HashMap<>();
        for (RegionFlag flag : RegionFlag.values()) {
            flags.put(flag, FlagState.EVERYONE);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public Map<RegionFlag, FlagState> getFlags() {
        return flags;
    }

    public boolean isShowingParticles() {
        return showParticles;
    }

    public void setShowParticles(boolean showParticles) {
        this.showParticles = showParticles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }

    public void addWhitelist(String uuid, String playerName) {
        String normalizedUUID = uuid.toLowerCase();
        if (owner.equals(normalizedUUID) || whitelist.containsKey(normalizedUUID)) {
            return;
        }
        whitelist.put(normalizedUUID, playerName);
    }

    public void removeWhitelist(String uuid) {
        String normalizedUUID = uuid.toLowerCase();
        if (owner.equals(normalizedUUID)) {
            return;
        }
        whitelist.remove(normalizedUUID);
    }

    public Map<String, String> getWhitelistMap() {
        return whitelist;
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
}
