package org.mateh.region.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.interfaces.SubCommand;

public class AddSubCommand implements SubCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /region add <regionID> <player>");
            return true;
        }
        String regionId = args[1];
        String targetName = args[2];
        Region region = Main.getInstance().getRegionManager().getRegion(regionId);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        String playerUUID = player.getUniqueId().toString().toLowerCase();
        if (!region.getOwner().equals(playerUUID) && !player.hasPermission("region.bypass")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to add players to this region.");
            return true;
        }
        if (player.getName().equalsIgnoreCase(targetName)) {
            player.sendMessage(ChatColor.RED + "You cannot add yourself to the whitelist.");
            return true;
        }
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Player " + targetName + " is not online.");
            return true;
        }
        String targetUUID = target.getUniqueId().toString().toLowerCase();
        if (region.getWhitelistMap().containsKey(targetUUID)) {
            player.sendMessage(ChatColor.RED + target.getName() + " is already in the whitelist.");
            return true;
        }
        region.addWhitelist(targetUUID, target.getName());
        player.sendMessage(ChatColor.GREEN + target.getName() + " added to the whitelist of " + region.getName());
        return true;
    }

    @Override
    public String getName() {
        return "add";
    }
}
