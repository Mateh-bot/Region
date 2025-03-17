package org.mateh.region.managers;

import org.mateh.region.models.RegionMenuAction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionMenuActionManager {
    private static final Map<UUID, RegionMenuAction> actions = new HashMap<>();

    public static void setAction(UUID playerUUID, RegionMenuAction action) {
        actions.put(playerUUID, action);
    }

    public static RegionMenuAction getAction(UUID playerUUID) {
        return actions.get(playerUUID);
    }

    public static void removeAction(UUID playerUUID) {
        actions.remove(playerUUID);
    }
}
