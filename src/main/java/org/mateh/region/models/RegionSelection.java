package org.mateh.region.models;

import org.bukkit.Location;

public class RegionSelection {
    private Location point1;
    private Location point2;

    public Location getPoint1() {
        return point1;
    }

    public void setPoint1(Location point1) {
        this.point1 = point1;
    }

    public Location getPoint2() {
        return point2;
    }

    public void setPoint2(Location point2) {
        this.point2 = point2;
    }
}
