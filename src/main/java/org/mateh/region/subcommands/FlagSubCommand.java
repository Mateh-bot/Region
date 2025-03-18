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
        if (!player.hasPermission("region.flag") && !player.hasPermission("region.bypass")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to modify flags in a region.");
            return true;
        }
        if (args.length < 4) {
            player.sendMessage(ChatColor.RED + "Usage: /region flag <regionID> <flag> <state>");
            return true;
        }
        String regionId = args[1];
        String flagName = args[2];
        String stateStr = args[3];
        Region region = Main.getInstance().getRegionManager().getRegion(regionId);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region does not exist.");
            return true;
        }
        String senderUUID = player.getUniqueId().toString();
        if (!region.getOwner().equals(senderUUID) && !player.hasPermission("region.bypass")) {
            player.sendMessage(ChatColor.RED + "You are not the owner of this region.");
            return true;
        }
        try {
            RegionFlag flag = RegionFlag.valueOf(flagName.toUpperCase());
            FlagState state = FlagState.valueOf(stateStr.toUpperCase());
            region.setFlag(flag, state);
            player.sendMessage(ChatColor.GREEN + "Flag " + flag.name() + " for region " + region.getName() + " updated to " + state.name());
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
