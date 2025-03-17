package org.mateh.region.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.interfaces.SubCommand;
import org.mateh.region.managers.GUIManager;
import org.mateh.region.subcommands.*;

import java.util.HashMap;
import java.util.Map;

public class RegionCommand implements CommandExecutor {
    private final Map<String, SubCommand> subCommands;

    public RegionCommand() {
        subCommands = new HashMap<>();
        subCommands.put("create", new CreateSubCommand());
        subCommands.put("wand", new WandSubCommand());
        subCommands.put("add", new AddSubCommand());
        subCommands.put("remove", new RemoveSubCommand());
        subCommands.put("whitelist", new WhitelistSubCommand());
        subCommands.put("flag", new FlagSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            GUIManager.openRegionsMenu(player);
            return true;
        }

        String sub = args[0].toLowerCase();
        if (subCommands.containsKey(sub)) {
            return subCommands.get(sub).execute(player, args);
        } else {
            Region region = Main.getInstance().getRegionManager().getRegion(args[0]);
            if (region == null) {
                player.sendMessage(ChatColor.RED + "Region does not exist.");
                return true;
            }
            GUIManager.openRegionMenu(player, region);
            return true;
        }
    }
}
