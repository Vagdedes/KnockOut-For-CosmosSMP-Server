package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerConnection implements Listener {

    @EventHandler
    private void Join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.setWalkSpeed(0.2f);
        PluginObjects.cachePlayerData(p);
    }

    @EventHandler
    private void Leave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = PluginObjects.getPlayerData(p);

        if (playerData.isKnockedOut()) {
            playerData.setDisconnected(true); // Always first (Prevents the death event & scheduler from being processed)
            GameMode gameMode = p.getGameMode();

            if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
                p.setHealth(0.0);

                Location location = p.getLocation();
                playerData.dropItems(location);
                p.teleport(location.getWorld().getSpawnLocation());
            }
        }
        PluginObjects.removePlayerData(playerData);
    }
}
