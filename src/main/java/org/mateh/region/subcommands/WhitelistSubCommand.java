package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.interfaces.SubCommand;
import org.mateh.region.models.Region;

import java.util.Map;

public class WhitelistSubCommand implements SubCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /region whitelist <regionID>");
            return true;
        }
        String regionId = args[1];
        Region region = Main.getInstance().getRegionManager().getRegion(regionId);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        Map<String, String> whitelistMap = region.getWhitelistMap();
        String list = String.join(", ", whitelistMap.values());
        player.sendMessage(ChatColor.GREEN + "Whitelist for " + region.getName() + ": " + list);
        return true;
    }

    @Override
    public String getName() {
        return "whitelist";
    }
}
