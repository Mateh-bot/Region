package org.mateh.region.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.mateh.region.models.Region;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RegionSQL {
    private SQLiteManager sqlite;

    public RegionSQL(SQLiteManager sqlite) {
        this.sqlite = sqlite;
    }

    public void saveRegion(Region region) {
        try {
            Connection conn = sqlite.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT OR REPLACE INTO regions (name, world, x1, y1, z1, x2, y2, z2, whitelist, flags, particles) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"
            );
            ps.setString(1, region.getName());
            ps.setString(2, region.getLoc1().getWorld().getName());
            ps.setDouble(3, region.getLoc1().getX());
            ps.setDouble(4, region.getLoc1().getY());
            ps.setDouble(5, region.getLoc1().getZ());
            ps.setDouble(6, region.getLoc2().getX());
            ps.setDouble(7, region.getLoc2().getY());
            ps.setDouble(8, region.getLoc2().getZ());
            String whitelist = String.join(",", region.getWhitelist());
            ps.setString(9, whitelist);
            StringBuilder flagsBuilder = new StringBuilder();
            for (Map.Entry<RegionFlag, FlagState> entry : region.getFlags().entrySet()) {
                flagsBuilder.append(entry.getKey().name()).append(":").append(entry.getValue().name()).append(";");
            }
            ps.setString(10, flagsBuilder.toString());
            ps.setInt(11, region.isShowingParticles() ? 1 : 0);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Region> loadRegions() {
        Map<String, Region> regions = new HashMap<>();
        try {
            Connection conn = sqlite.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM regions;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String worldName = rs.getString("world");
                double x1 = rs.getDouble("x1");
                double y1 = rs.getDouble("y1");
                double z1 = rs.getDouble("z1");
                double x2 = rs.getDouble("x2");
                double y2 = rs.getDouble("y2");
                double z2 = rs.getDouble("z2");
                World world = Bukkit.getWorld(worldName);
                if (world == null) continue;
                Location loc1 = new Location(world, x1, y1, z1);
                Location loc2 = new Location(world, x2, y2, z2);
                Region region = new Region(name, loc1, loc2);

                String whitelistStr = rs.getString("whitelist");
                if (whitelistStr != null && !whitelistStr.isEmpty()) {
                    String[] users = whitelistStr.split(",");
                    for (String user : users) {
                        region.addWhitelist(user.trim());
                    }
                }

                String flagsStr = rs.getString("flags");
                if (flagsStr != null && !flagsStr.isEmpty()) {
                    String[] flagPairs = flagsStr.split(";");
                    for (String pair : flagPairs) {
                        if (pair.isEmpty()) continue;
                        String[] parts = pair.split(":");
                        if (parts.length < 2) continue;
                        try {
                            RegionFlag flag = RegionFlag.valueOf(parts[0]);
                            FlagState state = FlagState.valueOf(parts[1]);
                            region.setFlag(flag, state);
                        } catch (IllegalArgumentException ex) {
                        }
                    }
                }
                region.setShowParticles(rs.getInt("particles") == 1);

                regions.put(name.toLowerCase(), region);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regions;
    }
}
