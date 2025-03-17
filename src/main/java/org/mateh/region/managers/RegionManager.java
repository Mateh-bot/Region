package org.mateh.region.managers;

import org.mateh.region.models.Region;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {
    private Map<String, Region> regions;

    public RegionManager() {
        regions = new HashMap<>();
    }

    public void addRegion(Region region) {
        regions.put(region.getName().toLowerCase(), region);
    }

    public Region getRegion(String name) {
        return regions.get(name.toLowerCase());
    }

    public void removeRegion(String name) {
        regions.remove(name.toLowerCase());
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, Region> regions) {
        this.regions = regions;
    }
}
