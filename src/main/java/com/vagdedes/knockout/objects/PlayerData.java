package com.vagdedes.knockout.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private ItemStack[] inventory;
    private ItemStack[] armor;
    private int exp, sneakingTicks, blockX, blockZ;
    private long knockedOut;
    private boolean disconnected;
    private Player player;

    // Initiation

    public PlayerData(Player player, ItemStack[] inventory, ItemStack[] armor, int exp) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.inventory = inventory;
        this.armor = armor;
        this.exp = exp;
        this.knockedOut = 0L;
        this.disconnected = false;
        this.sneakingTicks = 0;
    }

    // Get

    public Player getPlayer() {
        return player;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public int getExp() {
        return exp;
    }

    public long getKnockedOutTime() {
        return knockedOut;
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockZ() {
        return blockZ;
    }

    // Logic

    public boolean isKnockedOut(long l) {
        return l != 0L;
    }

    public boolean isKnockedOut() {
        return isKnockedOut(knockedOut);
    }

    public boolean hasDisconnected() {
        return disconnected;
    }

    // Set

    public void setInventory(ItemStack[] is) {
        inventory = is;
    }

    public void setArmor(ItemStack[] is) {
        armor = is;
    }

    public void setExp(int i) {
        if (i > exp) {
            exp = i;
        }
    }

    public void setKnockedOut(Location location) {
        if (location != null) {
            knockedOut = System.currentTimeMillis();
            blockX = location.getBlockX();
            blockZ = location.getBlockZ();
        } else {
            knockedOut = 0L;
            blockX = 0;
            blockZ = 0;
            resetSneakingTicks();
        }
    }

    public void setDisconnected(boolean b) {
        disconnected = b;
    }

    // Complex

    public boolean restore(double health) {
        if (isKnockedOut()) {
            if (player != null && player.isOnline()) {
                Location blockLocation = player.getLocation().clone().add(0, 1, 0);

                for (Player o : Bukkit.getOnlinePlayers()) {
                    o.sendBlockChange(blockLocation, blockLocation.getBlock().getBlockData());
                }

                PlayerInventory playerInventory = player.getInventory();
                playerInventory.setContents(inventory);
                playerInventory.setArmorContents(armor);

                player.setLevel(exp);
                player.setHealth(health);
                player.setWalkSpeed(0.2f);
                setKnockedOut(null);
                return true;
            }
        }
        return false;
    }

    public void dropItems(Location location) {
        World world = location.getWorld();

        if (world != null) {
            for (ItemStack itemStack : inventory) {
                if (itemStack != null) {
                    world.dropItem(location, itemStack);
                }
            }
            for (ItemStack itemStack : armor) {
                if (itemStack != null) {
                    world.dropItem(location, itemStack);
                }
            }
            inventory = new ItemStack[]{};
            armor = new ItemStack[]{};
        }
        exp = 0;
    }

    public int increaseSneakingTicks() {
        return sneakingTicks += 1;
    }

    public int resetSneakingTicks() {
        return sneakingTicks = 0;
    }
}
