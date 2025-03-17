package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.interfaces.SubCommand;

public class AddSubCommand implements SubCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /region add <name> <username>");
            return true;
        }
        String regionName = args[1];
        String username = args[2];
        Region region = Main.getInstance().getRegionManager().getRegion(regionName);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        region.addWhitelist(username);
        player.sendMessage(ChatColor.GREEN + username + " added to the whitelist of " + regionName);
        return true;
    }

    @Override
    public String getName() {
        return "add";
    }
}
