package org.mateh.region.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mateh.region.Main;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;
import org.mateh.region.models.Region;
import org.mateh.region.enums.GUIType;
import org.mateh.region.guis.PlayerGUI;
import org.mateh.region.utils.ItemUtils;

public class GUIManager {

    public static void openRegionsMenu(Player player) {
        int size = 27;
        PlayerGUI gui = new PlayerGUI(GUIType.REGIONS_MENU, null);
        Inventory inv = Bukkit.createInventory(gui, size, "Regions");
        gui.setInventory(inv);
        String playerUUID = player.getUniqueId().toString().toLowerCase();
        int slot = 0;
        for (Region region : Main.getInstance().getRegionManager().getRegions().values()) {
            if (region.getOwner().equals(playerUUID) || region.getWhitelistMap().containsKey(playerUUID)) {
                ItemStack item = ItemUtils.createItem(Material.PAPER, ChatColor.AQUA + region.getName());
                ItemMeta meta = item.getItemMeta();
                meta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), "region_id"), PersistentDataType.STRING, region.getId());
                item.setItemMeta(meta);
                inv.setItem(slot++, item);
            }
        }
        player.openInventory(inv);
    }

    public static void openRegionMenu(Player player, Region region) {
        int size = 9;
        PlayerGUI gui = new PlayerGUI(GUIType.REGION_MENU, region.getId());
        Inventory inv = Bukkit.createInventory(gui, size, "Region: " + region.getName());
        gui.setInventory(inv);

        inv.setItem(0, ItemUtils.createItem(Material.NAME_TAG, ChatColor.GREEN + "Rename"));
        inv.setItem(1, ItemUtils.createItem(Material.PAPER, ChatColor.GREEN + "Add to Whitelist"));
        inv.setItem(2, ItemUtils.createItem(Material.REDSTONE, ChatColor.GREEN + "Remove from Whitelist"));
        inv.setItem(3, ItemUtils.createItem(Material.COMPASS, ChatColor.GREEN + "Redefine Location"));
        inv.setItem(4, ItemUtils.createItem(Material.ANVIL, ChatColor.GREEN + "Edit Flags"));
        String particleStatus = region.isShowingParticles() ? "ON" : "OFF";
        inv.setItem(8, ItemUtils.createItem(Material.LEVER, ChatColor.GREEN + "Toggle Particles: " + particleStatus));
        player.openInventory(inv);
    }

    public static void openFlagsMenu(Player player, Region region) {
        int builtInCount = RegionFlag.values().length;
        int size = ((builtInCount - 1) / 9 + 1) * 9;
        PlayerGUI gui = new PlayerGUI(GUIType.FLAGS_MENU, region.getId());
        Inventory inv = Bukkit.createInventory(gui, size, "Flags: " + region.getName());
        gui.setInventory(inv);
        int slot = 0;
        for (RegionFlag flag : RegionFlag.values()) {
            FlagState state = region.getFlagState(flag);
            inv.setItem(slot++, ItemUtils.createItem(Material.STONE_BUTTON, ChatColor.AQUA + flag.name() + " - " + state.name()));
        }
        player.openInventory(inv);
    }
}
