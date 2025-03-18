package org.mateh.region.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;
import org.mateh.region.managers.RegionManager;
import org.mateh.region.models.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegionTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player player)) return completions;
        String playerUUID = player.getUniqueId().toString().toLowerCase();
        RegionManager rm = Main.getInstance().getRegionManager();

        String[] subcommands = {"create", "wand", "add", "remove", "whitelist", "flag"};
        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();
            for (String sub : subcommands) {
                if (sub.startsWith(currentArg)) {
                    completions.add(sub);
                }
            }
            Map<String, Region> created = rm.getRegionsCreatedBy(playerUUID);
            Map<String, Region> whitelisted = rm.getRegionsWhitelistedFor(playerUUID);
            created.forEach((id, region) -> {
                String suggestion = region.getName();
                if (suggestion.toLowerCase().startsWith(currentArg)) {
                    completions.add(suggestion);
                }
            });
            whitelisted.forEach((id, region) -> {
                String suggestion = region.getName();
                if (suggestion.toLowerCase().startsWith(currentArg)) {
                    completions.add(suggestion);
                }
            });
        } else if (args.length == 2) {
            String sub = args[0].toLowerCase();
            if (sub.equals("add") || sub.equals("remove") || sub.equals("whitelist") || sub.equals("flag")) {
                String currentArg = args[1].toLowerCase();
                Map<String, Region> created = rm.getRegionsCreatedBy(playerUUID);
                Map<String, Region> whitelisted = rm.getRegionsWhitelistedFor(playerUUID);
                created.forEach((id, region) -> {
                    String suggestion = region.getName();
                    if (suggestion.toLowerCase().startsWith(currentArg)) {
                        completions.add(suggestion);
                    }
                });
                whitelisted.forEach((id, region) -> {
                    String suggestion = region.getName();
                    if (suggestion.toLowerCase().startsWith(currentArg)) {
                        completions.add(suggestion);
                    }
                });
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("flag")) {
            String currentArg = args[2].toUpperCase();
            for (RegionFlag flag : RegionFlag.values()) {
                if (flag.name().startsWith(currentArg)) {
                    completions.add(flag.name());
                }
            }
        } else if (args.length == 4 && args[0].equalsIgnoreCase("flag")) {
            String currentArg = args[3].toUpperCase();
            for (FlagState state : FlagState.values()) {
                if (state.name().startsWith(currentArg)) {
                    completions.add(state.name());
                }
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("add")) {
            String currentArg = args[2].toLowerCase();
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.getName().toLowerCase().startsWith(currentArg)) {
                    String regionName = args[1];
                    Region region = rm.getRegion(regionName);
                    if (region != null && !region.getWhitelistMap().containsKey(online.getUniqueId().toString())) {
                        completions.add(online.getName());
                    }
                }
            }
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("remove")) {
            String currentArg = args[2].toLowerCase();
            String regionName = args[1];
            Region region = rm.getRegion(regionName);
            if (region != null) {
                for (String uuid : region.getWhitelistMap().keySet()) {
                    String name = region.getWhitelistMap().get(uuid);
                    if (name.toLowerCase().startsWith(currentArg)) {
                        completions.add(name);
                    }
                }
            }
        }
        return completions;
    }
}
