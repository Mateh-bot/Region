package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.models.RegionSelection;
import org.mateh.region.interfaces.SubCommand;
import org.mateh.region.managers.RegionSelectionManager;
import org.mateh.region.threads.ParticleThread;

public class CreateSubCommand implements SubCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /region create <name>");
            return true;
        }
        String regionName = args[1];
        RegionSelection selection = RegionSelectionManager.getSelection(player.getUniqueId());
        if (selection == null || selection.getPoint1() == null || selection.getPoint2() == null) {
            player.sendMessage(ChatColor.RED + "You must select both corners using the region wand (right-click on blocks).");
            return true;
        }
        Region region = new Region(regionName, selection.getPoint1(), selection.getPoint2());
        Main.getInstance().getRegionManager().addRegion(region);
        player.sendMessage(ChatColor.GREEN + "Region " + regionName + " created.");
        ParticleThread.drawRegionParticles(region);
        RegionSelectionManager.clearSelection(player.getUniqueId());
        return true;
    }

    @Override
    public String getName() {
        return "create";
    }
}
