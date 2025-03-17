package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.interfaces.SubCommand;

public class WhitelistSubCommand implements SubCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /region whitelist <name>");
            return true;
        }
        String region = args[1];
        Region regionWL = Main.getInstance().getRegionManager().getRegion(region);
        if (regionWL == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Whitelist for " + region + ": " + regionWL.getWhitelist().toString());
        return true;
    }

    @Override
    public String getName() {
        return "whitelist";
    }
}
