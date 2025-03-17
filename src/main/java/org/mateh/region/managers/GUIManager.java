package org.mateh.region.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.mateh.region.Main;
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

        int slot = 0;
        for (Region region : Main.getInstance().getRegionManager().getRegions().values()) {
            inv.setItem(slot++, ItemUtils.createItem(Material.PAPER, ChatColor.AQUA + region.getName()));
        }
        player.openInventory(inv);
    }

    public static void openRegionMenu(Player player, Region region) {
        int size = 9;
        PlayerGUI gui = new PlayerGUI(GUIType.REGION_MENU, region.getName());
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
        int builtInCount = org.mateh.region.enums.RegionFlag.values().length;
        int size = ((builtInCount - 1) / 9 + 1) * 9;
        PlayerGUI gui = new PlayerGUI(GUIType.FLAGS_MENU, region.getName());
        Inventory inv = Bukkit.createInventory(gui, size, "Flags: " + region.getName());
        gui.setInventory(inv);

        int slot = 0;
        for (org.mateh.region.enums.RegionFlag flag : org.mateh.region.enums.RegionFlag.values()) {
            org.mateh.region.enums.FlagState state = region.getFlagState(flag);
            inv.setItem(slot++, ItemUtils.createItem(Material.STONE_BUTTON, ChatColor.AQUA + flag.name() + " - " + state.name()));
        }
        player.openInventory(inv);
    }
}
