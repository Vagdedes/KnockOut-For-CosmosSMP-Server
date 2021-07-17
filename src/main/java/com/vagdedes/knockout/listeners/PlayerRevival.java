package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerRevival implements Listener {

    @EventHandler // Event have no priority or check-up for cancelled scenarios to ensure this critical feature works as expected.
    public void Interact(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player p) {
            Player t = e.getPlayer();

            if (!p.equals(t) && !PluginObjects.getPlayerData(t).isKnockedOut()) {
                PluginObjects.getPlayerData(p).restore();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Health(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player p && PluginObjects.getPlayerData(p).isKnockedOut()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Damage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && PluginObjects.getPlayerData(p).isKnockedOut()) {
            e.setCancelled(true);
        }
    }
}
