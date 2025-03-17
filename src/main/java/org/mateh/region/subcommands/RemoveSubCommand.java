package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.interfaces.SubCommand;

public class RemoveSubCommand implements SubCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /region remove <name> <username>");
            return true;
        }
        String regionName = args[1];
        String username = args[2];
        Region region = Main.getInstance().getRegionManager().getRegion(regionName);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        region.removeWhitelist(username);
        player.sendMessage(ChatColor.GREEN + username + " removed from the whitelist of " + regionName);
        return true;
    }

    @Override
    public String getName() {
        return "remove";
    }
}
