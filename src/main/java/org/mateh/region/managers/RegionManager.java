package org.mateh.region.managers;

import org.mateh.region.models.Region;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RegionManager {
    private Map<String, Region> regions;

    public RegionManager() {
        regions = new HashMap<>();
    }

    public void addRegion(Region region) {
        regions.put(region.getId(), region);
    }

    public Region getRegion(String key) {
        if (regions.containsKey(key)) {
            return regions.get(key);
        }
        for (Region region : regions.values()) {
            if (region.getName().equalsIgnoreCase(key)) {
                return region;
            }
        }
        return null;
    }

    public void removeRegion(String key) {
        regions.remove(key);
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, Region> regions) {
        this.regions = regions;
    }

    public Map<String, Region> getRegionsCreatedBy(String playerUUID) {
        return regions.values().stream()
                .filter(r -> r.getOwner().equals(playerUUID))
                .collect(Collectors.toMap(Region::getId, r -> r));
    }

    public Map<String, Region> getRegionsWhitelistedFor(String playerUUID) {
        return regions.values().stream()
                .filter(r -> r.getWhitelistMap().containsKey(playerUUID))
                .collect(Collectors.toMap(Region::getId, r -> r));
    }
}
