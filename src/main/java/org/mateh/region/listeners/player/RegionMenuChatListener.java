package org.mateh.region.listeners.player;

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
        switch (action.getActionType()) {
            case RENAME -> {
                String oldName = region.getName();

                Region newRegion = new Region(message, region.getLoc1(), region.getLoc2());
                newRegion.getWhitelist().addAll(region.getWhitelist());
                newRegion.getFlags().putAll(region.getFlags());
                Main.getInstance().getRegionManager().removeRegion(oldName);
                Main.getInstance().getRegionManager().addRegion(newRegion);
                player.sendMessage(ChatColor.GREEN + "Region renamed to " + message);
            }
            case ADD_WHITELIST -> {
                region.addWhitelist(message);
                player.sendMessage(ChatColor.GREEN + message + " added to the whitelist.");
            }
            case REMOVE_WHITELIST -> {
                region.removeWhitelist(message);
                player.sendMessage(ChatColor.GREEN + message + " removed from the whitelist.");
            }
            case REDEFINE_LOCATION -> {
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
