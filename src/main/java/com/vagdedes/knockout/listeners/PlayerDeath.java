package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Death(PlayerDeathEvent e) {
        Player p = e.getEntity();
        PlayerData playerData = PluginObjects.getPlayerData(p);

        if (!playerData.hasDisconnected() && !playerData.isKnockedOut()) {
            GameMode gameMode = p.getGameMode();

            if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
                e.setCancelled(true);
                Location location = p.getLocation();
                playerData.setKnockedOut(location); // Always first

                PlayerInventory playerInventory = p.getInventory();
                playerInventory.clear();
                playerInventory.setArmorContents(null);

                p.setLevel(0);
                p.setExp(0.0f);
                p.setWalkSpeed(0.0f);
                p.getActivePotionEffects().clear();
                e.getDrops().clear();

                if (p.getAllowFlight()) {
                    p.setFlying(false);
                }
                for (Player o : Bukkit.getOnlinePlayers()) {
                    if (p.equals(o) || o.getLocation().distance(location) <= 32) {
                        o.playSound(location, Sound.ENTITY_PLAYER_DEATH, 1f, 1f);
                    }
                }
                //location.getWorld().spawnParticle(Particle.REDSTONE, location, 16, 0.0, 0.0, 0.0, 0.0);
            } else {
                p.setWalkSpeed(0.2f);
                playerData.setKnockedOut(null);
            }
        } else {
            p.setWalkSpeed(0.2f);
            playerData.setKnockedOut(null);
        }
    }

    @EventHandler
    private void Respawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        p.setWalkSpeed(0.2f);

        PlayerData playerData = PluginObjects.getPlayerData(p);
        playerData.setKnockedOut(null);
        playerData.setExp(p.getLevel());

        PlayerInventory playerInventory = p.getInventory();
        playerData.setInventory(playerInventory.getContents());
        playerData.setArmor(playerInventory.getArmorContents());
    }
}
