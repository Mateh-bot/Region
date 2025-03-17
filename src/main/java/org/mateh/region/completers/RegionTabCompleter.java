package org.mateh.region.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;

import java.util.ArrayList;
import java.util.List;

public class RegionTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player)) {
            return completions;
        }

        String[] subcommands = {"create", "wand", "add", "remove", "whitelist", "flag"};

        if (args.length == 1) {
            String currentArg = args[0].toLowerCase();
            for (String sub : subcommands) {
                if (sub.startsWith(currentArg)) {
                    completions.add(sub);
                }
            }
            for (Region region : Main.getInstance().getRegionManager().getRegions().values()) {
                if (region.getName().toLowerCase().startsWith(currentArg)) {
                    completions.add(region.getName());
                }
            }
        } else if (args.length == 2) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("add") || subcommand.equals("remove") || subcommand.equals("flag") || subcommand.equals("whitelist")) {
                String currentArg = args[1].toLowerCase();
                for (Region region : Main.getInstance().getRegionManager().getRegions().values()) {
                    if (region.getName().toLowerCase().startsWith(currentArg)) {
                        completions.add(region.getName());
                    }
                }
            }
        } else if (args.length == 3) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("add") || subcommand.equals("remove")) {
            } else if (subcommand.equals("flag")) {
                String currentArg = args[2].toUpperCase();
                for (RegionFlag flag : RegionFlag.values()) {
                    if (flag.name().startsWith(currentArg)) {
                        completions.add(flag.name());
                    }
                }
            }
        } else if (args.length == 4) {
            String subcommand = args[0].toLowerCase();
            if (subcommand.equals("flag")) {
                String currentArg = args[3].toUpperCase();
                for (FlagState state : FlagState.values()) {
                    if (state.name().startsWith(currentArg)) {
                        completions.add(state.name());
                    }
                }
            }
        }

        return completions;
    }
}
