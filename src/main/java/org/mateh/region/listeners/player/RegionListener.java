package org.mateh.region.listeners.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mateh.region.models.Region;
import org.mateh.region.enums.FlagState;
import org.mateh.region.enums.RegionFlag;
import org.mateh.region.utils.RegionUtils;

public class RegionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Region region = RegionUtils.getRegionAtLocation(event.getBlock().getLocation());
        if (region != null) {
            FlagState state = region.getFlagState(RegionFlag.BLOCK_BREAK);
            if (state == FlagState.NONE && !player.hasPermission("region.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot break blocks in this region.");
            } else if (state == FlagState.WHITELIST &&
                    !region.getWhitelistMap().containsKey(player.getUniqueId().toString()) &&
                    !player.hasPermission("region.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have permission to break blocks in this region.");
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Region region = RegionUtils.getRegionAtLocation(event.getBlock().getLocation());
        if (region != null) {
            FlagState state = region.getFlagState(RegionFlag.BLOCK_PLACE);
            if (state == FlagState.NONE && !player.hasPermission("region.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot place blocks in this region.");
            } else if (state == FlagState.WHITELIST &&
                    !region.getWhitelistMap().containsKey(player.getUniqueId().toString()) &&
                    !player.hasPermission("region.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have permission to place blocks in this region.");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Region region = RegionUtils.getRegionAtLocation(
                event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : player.getLocation()
        );
        if (region != null) {
            FlagState state = region.getFlagState(RegionFlag.INTERACT);
            if (state == FlagState.NONE && !player.hasPermission("region.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot interact in this region.");
            } else if (state == FlagState.WHITELIST &&
                    !region.getWhitelistMap().containsKey(player.getUniqueId().toString()) &&
                    !player.hasPermission("region.bypass")) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You do not have permission to interact in this region.");
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            Region region = RegionUtils.getRegionAtLocation(player.getLocation());
            if (region != null) {
                FlagState state = region.getFlagState(RegionFlag.ENTITY_DAMAGE);
                if (state == FlagState.NONE && !player.hasPermission("region.bypass")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot damage entities in this region.");
                } else if (state == FlagState.WHITELIST &&
                        !region.getWhitelistMap().containsKey(player.getUniqueId().toString()) &&
                        !player.hasPermission("region.bypass")) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You do not have permission to damage entities in this region.");
                }
            }
        }
    }
}
