package org.mateh.region.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.mateh.region.Main;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;
import org.mateh.region.managers.MySQLManager;
import org.mateh.region.models.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionSQL {
    private final MySQLManager mysqlManager;

    public RegionSQL(MySQLManager mysqlManager) {
        this.mysqlManager = mysqlManager;
    }

    public void saveRegion(Region region) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            try {
                Connection conn = mysqlManager.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO regions (id, name, owner, world, x1, y1, z1, x2, y2, z2, whitelist, flags, particles) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                "ON DUPLICATE KEY UPDATE " +
                                "name = VALUES(name), owner = VALUES(owner), world = VALUES(world), " +
                                "x1 = VALUES(x1), y1 = VALUES(y1), z1 = VALUES(z1), " +
                                "x2 = VALUES(x2), y2 = VALUES(y2), z2 = VALUES(z2), " +
                                "whitelist = VALUES(whitelist), flags = VALUES(flags), particles = VALUES(particles)"
                );
                ps.setString(1, region.getId());
                ps.setString(2, region.getName());
                ps.setString(3, region.getOwner());
                ps.setString(4, region.getLoc1().getWorld().getName());
                ps.setDouble(5, region.getLoc1().getX());
                ps.setDouble(6, region.getLoc1().getY());
                ps.setDouble(7, region.getLoc1().getZ());
                ps.setDouble(8, region.getLoc2().getX());
                ps.setDouble(9, region.getLoc2().getY());
                ps.setDouble(10, region.getLoc2().getZ());
                String whitelist = String.join(",", region.getWhitelistMap().keySet());
                ps.setString(11, whitelist);
                StringBuilder flagsBuilder = new StringBuilder();
                for (Map.Entry<RegionFlag, FlagState> entry : region.getFlags().entrySet()) {
                    flagsBuilder.append(entry.getKey().name())
                            .append(":")
                            .append(entry.getValue().name())
                            .append(";");
                }
                ps.setString(12, flagsBuilder.toString());
                ps.setInt(13, region.isShowingParticles() ? 1 : 0);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public Map<String, Region> loadRegions() {
        Map<String, Region> regions = new HashMap<>();
        try {
            Connection conn = mysqlManager.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM regions;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String owner = rs.getString("owner");
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
                Region region = new Region(id, name, owner, loc1, loc2);

                String whitelistStr = rs.getString("whitelist");
                if (whitelistStr != null && !whitelistStr.isEmpty()) {
                    String[] users = whitelistStr.split(",");
                    for (String user : users) {
                        user = user.trim();
                        try {
                            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(user));
                            region.addWhitelist(user, op.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                String flagsStr = rs.getString("flags");
                if (flagsStr != null && !flagsStr.isEmpty()) {
                    String[] flagPairs = flagsStr.split(";");
                    for (String pair : flagPairs) {
                        if (pair.isEmpty()) continue;
                        String[] split = pair.split(":");
                        if (split.length != 2) continue;
                        try {
                            RegionFlag flag = RegionFlag.valueOf(split[0]);
                            FlagState state = FlagState.valueOf(split[1]);
                            region.setFlag(flag, state);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }

                region.setShowParticles(rs.getInt("particles") == 1);
                regions.put(id, region);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return regions;
    }
}
