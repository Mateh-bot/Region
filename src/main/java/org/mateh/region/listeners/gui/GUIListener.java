package org.mateh.region.listeners.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mateh.region.Main;
import org.mateh.region.models.Region;
import org.mateh.region.models.RegionMenuAction;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.GUIType;
import org.mateh.region.enums.RegionFlag;
import org.mateh.region.guis.PlayerGUI;
import org.mateh.region.managers.GUIManager;
import org.mateh.region.managers.RegionMenuActionManager;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof PlayerGUI playerGUI)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        String option = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());

        if (playerGUI.getGuiType() == GUIType.REGIONS_MENU) {
            ItemMeta meta = clicked.getItemMeta();
            String regionId = meta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "region_id"), PersistentDataType.STRING);
            if (regionId == null || regionId.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Invalid region item format.");
                return;
            }
            Region region = Main.getInstance().getRegionManager().getRegion(regionId);
            if (region != null) {
                GUIManager.openRegionMenu(player, region);
            } else {
                player.sendMessage(ChatColor.RED + "Region not found.");
            }
        } else if (playerGUI.getGuiType() == GUIType.REGION_MENU) {
            String regionId = playerGUI.getRegionName();
            Region region = Main.getInstance().getRegionManager().getRegion(regionId);
            if (region == null) return;
            if (option.startsWith("Toggle Particles")) {
                boolean current = region.isShowingParticles();
                region.setShowParticles(!current);
                player.sendMessage(ChatColor.GREEN + "Particle visualization "
                        + (region.isShowingParticles() ? "enabled" : "disabled")
                        + " for region " + region.getName());
                GUIManager.openRegionMenu(player, region);
                return;
            }
            switch (option) {
                case "Rename":
                    player.closeInventory();
                    RegionMenuActionManager.setAction(player.getUniqueId(),
                            new RegionMenuAction(player.getUniqueId(), region.getId(), RegionMenuAction.ActionType.RENAME));
                    player.sendMessage(ChatColor.GREEN + "Please type the new region name in chat.");
                    break;
                case "Add to Whitelist":
                    player.closeInventory();
                    RegionMenuActionManager.setAction(player.getUniqueId(),
                            new RegionMenuAction(player.getUniqueId(), region.getId(), RegionMenuAction.ActionType.ADD_WHITELIST));
                    player.sendMessage(ChatColor.GREEN + "Please type the username to add to the whitelist in chat.");
                    break;
                case "Remove from Whitelist":
                    player.closeInventory();
                    RegionMenuActionManager.setAction(player.getUniqueId(),
                            new RegionMenuAction(player.getUniqueId(), region.getId(), RegionMenuAction.ActionType.REMOVE_WHITELIST));
                    player.sendMessage(ChatColor.GREEN + "Please type the username to remove from the whitelist in chat.");
                    break;
                case "Redefine Location":
                    player.closeInventory();
                    RegionMenuActionManager.setAction(player.getUniqueId(),
                            new RegionMenuAction(player.getUniqueId(), region.getId(), RegionMenuAction.ActionType.REDEFINE_LOCATION));
                    player.sendMessage(ChatColor.GREEN + "Select new region corners with the region wand, then type 'confirm' in chat.");
                    break;
                case "Edit Flags":
                    GUIManager.openFlagsMenu(player, region);
                    break;
                default:
                    break;
            }
        } else if (playerGUI.getGuiType() == GUIType.FLAGS_MENU) {
            if (!option.contains(" - ")) return;
            String[] parts = option.split(" - ");
            if (parts.length < 2) return;
            String flagName = parts[0];
            Region region = Main.getInstance().getRegionManager().getRegion(playerGUI.getRegionName());
            if (region == null) return;
            try {
                RegionFlag flag = RegionFlag.valueOf(flagName);
                FlagState current = region.getFlagState(flag);
                FlagState next;
                switch (current) {
                    case EVERYONE:
                        next = FlagState.WHITELIST;
                        break;
                    case WHITELIST:
                        next = FlagState.NONE;
                        break;
                    case NONE:
                    default:
                        next = FlagState.EVERYONE;
                        break;
                }
                region.setFlag(flag, next);
                player.sendMessage(ChatColor.GREEN + "Flag " + flag.name() + " updated to " + next.name());
            } catch (IllegalArgumentException e) {
            }
            GUIManager.openFlagsMenu(player, region);
        }
    }
}
