package com.vagdedes.knockout;

import com.vagdedes.knockout.handlers.PluginObjects;
import com.vagdedes.knockout.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Register extends JavaPlugin {

    public static Plugin plugin;

    public void onEnable() {
        plugin = this;
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerDeath(), this);
        manager.registerEvents(new PlayerMovement(), this);
        manager.registerEvents(new PlayerConnection(), this);
        manager.registerEvents(new PlayerInteractions(), this);
        manager.registerEvents(new PlayerRevival(), this);
    }

    public void onDisable() {
        PluginObjects.clear();
        plugin = null;
    }
}
