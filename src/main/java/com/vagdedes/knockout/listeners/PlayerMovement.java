package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Move(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = PluginObjects.getPlayerData(p);

        if (playerData.isKnockedOut()) {
            Location location = p.getLocation();
            int blockX = playerData.getBlockX();
            int blockZ = playerData.getBlockZ();

            if (location.getBlockX() != blockX || location.getBlockZ() != blockZ) {
                p.teleport(new Location(location.getWorld(), blockX + 0.5, location.getY(), blockZ + 0.5, location.getYaw(), location.getPitch()));
            }
        }
    }
}
