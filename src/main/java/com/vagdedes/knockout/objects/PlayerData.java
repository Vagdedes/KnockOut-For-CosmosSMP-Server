package com.vagdedes.knockout.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private ItemStack[] inventory;
    private ItemStack[] armor;
    private int exp, sneakingTicks;
    private long knockedOut;
    private boolean disconnected;

    // Initiation

    public PlayerData(UUID uuid, ItemStack[] inventory, ItemStack[] armor, int exp) {
        this.uuid = uuid;
        this.inventory = inventory;
        this.armor = armor;
        this.exp = exp;
        this.knockedOut = 0L;
        this.disconnected = false;
        this.sneakingTicks = 0;
    }

    // Get

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

    public void setKnockedOut(boolean b) {
        if (b) {
            knockedOut = System.currentTimeMillis();
        } else {
            knockedOut = 0L;
            resetSneakingTicks();
        }
    }

    public void setDisconnected(boolean b) {
        disconnected = b;
    }

    // Complex

    public boolean restore() {
        if (isKnockedOut()) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null && player.isOnline()) {
                PlayerInventory playerInventory = player.getInventory();
                playerInventory.setContents(inventory);
                playerInventory.setArmorContents(armor);
                player.setLevel(exp);
                player.setHealth(player.getMaxHealth());
                setKnockedOut(false);
                return true;
            }
        }
        return false;
    }

    public void dropItems(Location location) {
        World world = location.getWorld();

        if (world != null) {
            for (ItemStack itemStack : inventory) {
                world.dropItem(location, itemStack);
            }
            for (ItemStack itemStack : armor) {
                world.dropItem(location, itemStack);
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
