package com.vagdedes.knockout.handlers;

import com.vagdedes.knockout.Register;
import com.vagdedes.knockout.objects.PlayerData;
import org.bukkit.Bukkit;
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
                            Player p = Bukkit.getPlayer(playerData.getUUID());

                            if (p != null && p.isOnline()) {
                                if ((currentTime - knockedOutTime) >= 60_000L) { // Death after 60 seconds of not being revived.
                                    playerData.dropItems(p.getLocation());
                                    p.setHealth(0.0);
                                    playerData.setKnockedOut(false); // Always last
                                } else {
                                    if (p.isSneaking()) {
                                        if (playerData.increaseSneakingTicks() >= 100) { // Death after sneaking for 5 seconds.
                                            playerData.dropItems(p.getLocation());
                                            p.setHealth(0.0);
                                            playerData.setKnockedOut(false); // Always last
                                        }
                                    } else {
                                        playerData.resetSneakingTicks();
                                    }
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
            iterator.next().restore();
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
        PlayerData playerData = new PlayerData(p.getUniqueId(),
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
