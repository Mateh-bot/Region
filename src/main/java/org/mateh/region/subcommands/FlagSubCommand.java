package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;
import org.mateh.region.interfaces.SubCommand;

public class FlagSubCommand implements SubCommand {
    @Override
    public boolean execute(Player player, String[] args) {
        if (args.length < 4) {
            player.sendMessage(ChatColor.RED + "Usage: /region flag <name> <flag> <state>");
            return true;
        }
        String regionName = args[1];
        String flagName = args[2];
        String stateStr = args[3];
        Region region = Main.getInstance().getRegionManager().getRegion(regionName);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        try {
            RegionFlag flag = RegionFlag.valueOf(flagName.toUpperCase());
            FlagState state = FlagState.valueOf(stateStr.toUpperCase());
            region.setFlag(flag, state);
            player.sendMessage(ChatColor.GREEN + "Flag " + flag + " for " + regionName + " updated to " + state);
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid flag or state.");
        }
        return true;
    }

    @Override
    public String getName() {
        return "flag";
    }
}
