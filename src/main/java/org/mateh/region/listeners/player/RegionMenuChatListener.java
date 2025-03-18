package org.mateh.region.listeners.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.models.RegionMenuAction;
import org.mateh.region.models.RegionSelection;
import org.mateh.region.managers.RegionMenuActionManager;
import org.mateh.region.managers.RegionSelectionManager;

public class RegionMenuChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        RegionMenuAction action = RegionMenuActionManager.getAction(player.getUniqueId());
        if (action == null) {
            return;
        }
        event.setCancelled(true);

        Region region = Main.getInstance().getRegionManager().getRegion(action.getRegionName());
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region not found.");
            RegionMenuActionManager.removeAction(player.getUniqueId());
            return;
        }

        String message = event.getMessage();
        String playerUUID = player.getUniqueId().toString().toLowerCase();

        switch (action.getActionType()) {
            case RENAME -> {
                if (!region.getOwner().equals(playerUUID) && !player.hasPermission("region.bypass")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to rename this region.");
                    break;
                }
                Region newRegion = new Region(region.getId(), message, region.getOwner(), region.getLoc1(), region.getLoc2());
                newRegion.getWhitelistMap().putAll(region.getWhitelistMap());
                newRegion.getFlags().putAll(region.getFlags());
                Main.getInstance().getRegionManager().removeRegion(region.getId());
                Main.getInstance().getRegionManager().addRegion(newRegion);
                player.sendMessage(ChatColor.GREEN + "Region renamed to " + message);
            }
            case ADD_WHITELIST -> {
                Player target = Bukkit.getPlayerExact(message);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player " + message + " is not online.");
                    break;
                }
                String targetUUID = target.getUniqueId().toString().toLowerCase();
                if (region.getOwner().equals(targetUUID)) {
                    player.sendMessage(ChatColor.RED + "The region owner cannot be added to the whitelist.");
                } else {
                    if (region.getWhitelistMap().containsKey(targetUUID)) {
                        player.sendMessage(ChatColor.RED + target.getName() + " is already in the whitelist.");
                    } else {
                        region.addWhitelist(targetUUID, target.getName());
                        player.sendMessage(ChatColor.GREEN + target.getName() + " added to the whitelist.");
                    }
                }
            }
            case REMOVE_WHITELIST -> {
                Player target = Bukkit.getPlayerExact(message);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Player " + message + " is not online.");
                    break;
                }
                String targetUUID = target.getUniqueId().toString().toLowerCase();
                if (region.getOwner().equals(targetUUID)) {
                    player.sendMessage(ChatColor.RED + "The region owner cannot be removed from the whitelist.");
                } else {
                    if (!region.getWhitelistMap().containsKey(targetUUID)) {
                        player.sendMessage(ChatColor.RED + target.getName() + " is not in the whitelist.");
                    } else {
                        region.removeWhitelist(targetUUID);
                        player.sendMessage(ChatColor.GREEN + target.getName() + " removed from the whitelist.");
                    }
                }
            }
            case REDEFINE_LOCATION -> {
                if (!region.getOwner().equals(playerUUID) && !player.hasPermission("region.bypass")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to redefine the location of this region.");
                    break;
                }
                if (!message.equalsIgnoreCase("confirm")) {
                    player.sendMessage(ChatColor.RED + "Type 'confirm' after selecting new region corners with the region wand.");
                    return;
                }
                RegionSelection selection = RegionSelectionManager.getSelection(player.getUniqueId());
                if (selection == null || selection.getPoint1() == null || selection.getPoint2() == null) {
                    player.sendMessage(ChatColor.RED + "You must select both corners using the region wand.");
                    return;
                }
                region.setLoc1(selection.getPoint1());
                region.setLoc2(selection.getPoint2());
                player.sendMessage(ChatColor.GREEN + "Region location redefined.");
                RegionSelectionManager.clearSelection(player.getUniqueId());
            }
        }
        RegionMenuActionManager.removeAction(player.getUniqueId());
    }
}
