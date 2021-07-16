package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnection implements Listener {

    @EventHandler
    private void Join(PlayerJoinEvent e) {
        PluginObjects.cachePlayerDataIfAbsent(e.getPlayer());
    }

    @EventHandler
    private void Leave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = PluginObjects.getPlayerData(p);

        if (playerData.isKnockedOut()) {
            playerData.setDisconnected(true); // Always first
            p.setHealth(0.0);
            playerData.dropItems(p.getLocation());
            playerData.setKnockedOut(false); // Always last
        }
        PluginObjects.removePlayerData(playerData);
    }
}
