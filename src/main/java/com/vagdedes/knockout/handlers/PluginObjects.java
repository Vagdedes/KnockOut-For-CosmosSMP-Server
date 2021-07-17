package com.vagdedes.knockout.handlers;

import com.vagdedes.knockout.Register;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

public class PluginObjects {

    // Variables

    private static final HashSet<PlayerData> playerDataSet = new HashSet<>(Bukkit.getMaxPlayers());

    // Scheduler

    static {
        if (Register.plugin != null && Register.plugin.isEnabled()) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Register.plugin, () -> {
                long currentTime = System.currentTimeMillis();

                Iterator<PlayerData> iterator = playerDataSet.iterator();

                while (iterator.hasNext()) {
                    PlayerData playerData = iterator.next();

                    if (!playerData.hasDisconnected()) {
                        long knockedOutTime = playerData.getKnockedOutTime();

                        if (playerData.isKnockedOut(knockedOutTime)) {
                            Player p = playerData.getPlayer();

                            if (p != null && p.isOnline()) {
                                GameMode gameMode = p.getGameMode();

                                if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
                                    p.setFireTicks(0);
                                    long timePassed = currentTime - knockedOutTime;
                                    long maxTimePassed = 60_000L;
                                    Location blockLocation = p.getLocation().clone().add(0, 1, 0);

                                    if (timePassed >= maxTimePassed) { // Death after 60 seconds in milliseconds of not being revived.
                                        playerData.dropItems(p.getLocation());
                                        p.setHealth(0.0);
                                        p.sendBlockChange(blockLocation, blockLocation.getBlock().getBlockData());
                                    } else {
                                        if (p.isSneaking()) {
                                            double maxTicks = 100;
                                            double ticksPassed = playerData.increaseSneakingTicks();

                                            if (ticksPassed >= maxTicks) { // Death after sneaking for 5 seconds in ticks.
                                                playerData.dropItems(p.getLocation());
                                                p.setHealth(0.0);
                                                p.sendBlockChange(blockLocation, blockLocation.getBlock().getBlockData());
                                            } else {
                                                double health = p.getMaxHealth();
                                                p.setHealth(Math.min(
                                                        health - ((health / maxTicks) * ticksPassed),
                                                        health - ((health / maxTimePassed) * timePassed))
                                                );
                                                p.sendBlockChange(blockLocation, Material.BARRIER, (byte) 0);
                                            }
                                        } else {
                                            playerData.resetSneakingTicks();
                                            double health = p.getMaxHealth();
                                            p.setHealth(health - ((health / maxTimePassed) * timePassed));
                                            p.sendBlockChange(blockLocation, Material.BARRIER, (byte) 0);
                                        }
                                    }
                                } else {
                                    playerData.restore(p.getMaxHealth());
                                }
                            } else {
                                iterator.remove(); // Remove player from loop in rare scenario they are not found to exist or be online.
                            }
                        }
                    }
                }
            }, 0L, 0L);
        }
    }

    // Base

    public static void clear() {
        Iterator<PlayerData> iterator = playerDataSet.iterator();

        while (iterator.hasNext()) {
            PlayerData playerData = iterator.next();
            playerData.restore(playerData.getPlayer().getMaxHealth());
            iterator.remove();
        }
    }

    // Player Data

    public static PlayerData[] getPlayerData() {
        return playerDataSet.toArray(new PlayerData[0]);
    }

    public static boolean removePlayerData(PlayerData playerData) {
        return playerDataSet.remove(playerData);
    }

    public static PlayerData cachePlayerData(Player p) {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p,
                playerInventory.getContents(),
                playerInventory.getArmorContents(),
                p.getLevel());
        playerDataSet.add(playerData);
        return playerData;
    }

    public static boolean cachePlayerDataIfAbsent(Player p) {
        UUID uuid = p.getUniqueId();

        for (PlayerData playerData : playerDataSet) {
            if (playerData.getUUID().equals(uuid)) {
                return false;
            }
        }
        cachePlayerData(p);
        return true;
    }

    public static PlayerData getPlayerData(Player p) {
        UUID uuid = p.getUniqueId();

        for (PlayerData playerData : playerDataSet) {
            if (playerData.getUUID().equals(uuid)) {
                return playerData;
            }
        }
        return cachePlayerData(p);
    }
}
