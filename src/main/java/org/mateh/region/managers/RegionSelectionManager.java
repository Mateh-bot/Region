package org.mateh.region.managers;

import org.mateh.region.models.RegionSelection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionSelectionManager {
    private static final Map<UUID, RegionSelection> selections = new HashMap<>();

    public static RegionSelection getSelection(UUID uuid) {
        return selections.get(uuid);
    }

    public static RegionSelection createOrGetSelection(UUID uuid) {
        RegionSelection selection = selections.get(uuid);
        if (selection == null) {
            selection = new RegionSelection();
            selections.put(uuid, selection);
        }
        return selection;
    }

    public static void clearSelection(UUID uuid) {
        selections.remove(uuid);
    }
}
