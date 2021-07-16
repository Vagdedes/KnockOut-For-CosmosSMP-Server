package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerRevival implements Listener {

    // Events have no priority or check-up for cancelled scenarios to ensure this critical feature works as expected.

    @EventHandler
    public void Interact(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player p) {
            Player t = e.getPlayer();

            if (!p.equals(t) && !PluginObjects.getPlayerData(t).isKnockedOut()) {
                PluginObjects.getPlayerData(p).restore();
            }
        }
    }
}
