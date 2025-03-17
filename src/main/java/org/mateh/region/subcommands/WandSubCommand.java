package org.mateh.region.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.mateh.region.Main;
import org.mateh.region.interfaces.SubCommand;
import org.mateh.region.utils.ItemUtils;

public class WandSubCommand implements SubCommand {

    @Override
    public boolean execute(Player player, String[] args) {
        ItemStack wand = ItemUtils.createItem(Material.STICK, ChatColor.GOLD + "Region Wand");
        ItemMeta meta = wand.getItemMeta();
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "region_wand");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
        wand.setItemMeta(meta);

        player.getInventory().addItem(wand);
        player.sendMessage(ChatColor.GREEN + "You have received the region wand.");
        return true;
    }

    @Override
    public String getName() {
        return "wand";
    }
}
