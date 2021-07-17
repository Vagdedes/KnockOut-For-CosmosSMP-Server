package com.vagdedes.knockout.listeners;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInteractions implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Level(PlayerLevelChangeEvent e) {
        PluginObjects.getPlayerData(e.getPlayer()).setExp(e.getNewLevel());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Drop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        PlayerData playerData = PluginObjects.getPlayerData(p);

        if (playerData.isKnockedOut()) {
            e.setCancelled(true);
        } else {
            PlayerInventory playerInventory = p.getInventory();
            playerData.setInventory(playerInventory.getContents());
            playerData.setArmor(playerInventory.getArmorContents());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Pick(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player p) {
            PlayerData playerData = PluginObjects.getPlayerData(p);

            if (playerData.isKnockedOut()) {
                e.setCancelled(true);
            } else {
                PlayerInventory playerInventory = p.getInventory();
                playerData.setInventory(playerInventory.getContents());
                playerData.setArmor(playerInventory.getArmorContents());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Inventory(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p) {
            PlayerData playerData = PluginObjects.getPlayerData(p);

            if (playerData.isKnockedOut()) {
                e.setCancelled(true);
                p.closeInventory();
            } else {
                PlayerInventory playerInventory = p.getInventory();
                playerData.setInventory(playerInventory.getContents());
                playerData.setArmor(playerInventory.getArmorContents());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void Interact(PlayerInteractEvent e) {
        PlayerData playerData = PluginObjects.getPlayerData(e.getPlayer());

        if (playerData.isKnockedOut()) {
            e.setCancelled(true);
        }
    }
}
