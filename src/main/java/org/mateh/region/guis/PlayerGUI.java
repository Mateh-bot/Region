package org.mateh.region.guis;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.mateh.region.enums.GUIType;

public class PlayerGUI implements InventoryHolder {
    private final GUIType guiType;
    private final String regionName;
    private Inventory inventory;

    public PlayerGUI(GUIType guiType, String regionName) {
        this.guiType = guiType;
        this.regionName = regionName;
    }

    public GUIType getGuiType() {
        return guiType;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
