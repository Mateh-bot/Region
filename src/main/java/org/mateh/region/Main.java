package org.mateh.region;

import org.bukkit.plugin.java.JavaPlugin;
import org.mateh.region.commands.RegionCommand;
import org.mateh.region.completers.RegionTabCompleter;
import org.mateh.region.data.RegionSQL;
import org.mateh.region.data.SQLiteManager;
import org.mateh.region.listeners.gui.GUIListener;
import org.mateh.region.listeners.player.RegionListener;
import org.mateh.region.listeners.player.RegionMenuChatListener;
import org.mateh.region.listeners.player.RegionSelectionListener;
import org.mateh.region.managers.RegionManager;
import org.mateh.region.models.Region;
import org.mateh.region.threads.ParticleThread;

public final class Main extends JavaPlugin {
    private static Main instance;
    private RegionManager regionManager;
    private SQLiteManager sqliteManager;
    private RegionSQL regionSQL;

    @Override
    public void onEnable() {
        instance = this;
        regionManager = new RegionManager();

        sqliteManager = new SQLiteManager(getDataFolder().getAbsolutePath() + "/data.db");
        regionSQL = new RegionSQL(sqliteManager);

        regionManager.setRegions(regionSQL.loadRegions());

        this.getCommand("region").setExecutor(new RegionCommand());
        this.getCommand("region").setTabCompleter(new RegionTabCompleter());

        getServer().getPluginManager().registerEvents(new RegionListener(), this);
        getServer().getPluginManager().registerEvents(new RegionSelectionListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new RegionMenuChatListener(), this);

        getServer().getScheduler().runTaskTimerAsynchronously(this, () ->{
            for (Region region : regionManager.getRegions().values()){
                regionSQL.saveRegion(region);
            }
        }, 0L, 1200L);

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Region region : regionManager.getRegions().values()) {
                if (region.isShowingParticles()) {
                    ParticleThread.drawRegionParticles(region);
                }
            }
        }, 0L, 10L);
    }

    @Override
    public void onDisable() {
        for (Region region : regionManager.getRegions().values()) {
            regionSQL.saveRegion(region);
        }
        try {
            sqliteManager.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}
