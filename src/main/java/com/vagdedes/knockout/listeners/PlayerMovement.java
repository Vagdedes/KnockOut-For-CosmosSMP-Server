package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMovement implements Listener {

    // Events have no priority or check-up for cancelled scenarios to ensure this critical feature works as expected.

    @EventHandler
    private void Glide(EntityToggleGlideEvent e) {
        if (e.isGliding() && e.getEntity() instanceof Player p && PluginObjects.getPlayerData(p).isKnockedOut()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    private void Move(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (PluginObjects.getPlayerData(p).isKnockedOut()) {
            p.setGliding(true);
        }
    }
}
