package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerDeath implements Listener {

    // Events have no priority or check-up for cancelled scenarios to ensure this critical feature works as expected.

    @EventHandler
    private void Death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData playerData = PluginObjects.getPlayerData(p);

        if (!playerData.hasDisconnected()) {
            e.setCancelled(true);

            if (!playerData.isKnockedOut()) {
                playerData.setKnockedOut(true); // Always first
                p.setGliding(true);
                p.setHealth(1.0);
                p.setLevel(0);
                p.setExp(0.0f);
                p.getActivePotionEffects().clear();
                e.getDrops().clear();
            }
        }
    }

    @EventHandler
    private void Respawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();

        PlayerData playerData = PluginObjects.getPlayerData(p);
        playerData.setKnockedOut(false);
        playerData.setExp(p.getLevel());

        PlayerInventory playerInventory = p.getInventory();
        playerData.setInventory(playerInventory.getContents());
        playerData.setArmor(playerInventory.getArmorContents());
    }
}
