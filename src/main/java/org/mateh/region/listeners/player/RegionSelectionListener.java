package org.mateh.region.listeners.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mateh.region.Main;
import org.mateh.region.managers.RegionSelectionManager;
import org.mateh.region.models.RegionSelection;

public class RegionSelectionListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.STICK) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "region_wand");
        if (!meta.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
            return;
        }

        String itemName = ChatColor.stripColor(meta.getDisplayName());
        if (!itemName.equalsIgnoreCase("Region Wand")) {
            return;
        }

        event.setCancelled(true);

        Location loc = event.getClickedBlock().getLocation();

        RegionSelection selection = RegionSelectionManager.createOrGetSelection(player.getUniqueId());
        if (selection.getPoint1() == null) {
            selection.setPoint1(loc);
            player.sendMessage(ChatColor.GREEN + "Point 1 selected: "
                    + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        } else if (selection.getPoint2() == null) {
            selection.setPoint2(loc);
            player.sendMessage(ChatColor.GREEN + "Point 2 selected: "
                    + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        } else {
            selection.setPoint1(loc);
            selection.setPoint2(null);
            player.sendMessage(ChatColor.GREEN + "Selection reset. New Point 1: "
                    + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        }
    }
}
