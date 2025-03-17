package org.mateh.region.utils;

import org.bukkit.Location;
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
}
