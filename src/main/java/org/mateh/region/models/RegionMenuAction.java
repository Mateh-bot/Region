package org.mateh.region.models;

import java.util.UUID;

public class RegionMenuAction {

    public enum ActionType {
        RENAME,
        ADD_WHITELIST,
        REMOVE_WHITELIST,
        REDEFINE_LOCATION
    }

    private final UUID playerUUID;
    private final String regionName;
    private final ActionType actionType;

    public RegionMenuAction(UUID playerUUID, String regionName, ActionType actionType) {
        this.playerUUID = playerUUID;
        this.regionName = regionName;
        this.actionType = actionType;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getRegionName() {
        return regionName;
    }

    public ActionType getActionType() {
        return actionType;
    }
}
