package org.mateh.region.interfaces;

import org.bukkit.entity.Player;

public interface SubCommand {
    boolean execute(Player player, String[] args);

    String getName();
}
