package com.gmail.jannyboy11.customrecipes.gui.framework;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class GuiInventoryHolder<P extends Plugin> implements InventoryHolder {
    
    private final Inventory inventory;
    private final P plugin;
    final GuiListener<P> guiListener;
    
    public GuiInventoryHolder(P plugin, InventoryType type, String title) {
        this.plugin = plugin;
        this.guiListener = new GuiListener<>(this);
        this.inventory = plugin.getServer().createInventory(this, type, title); //implicit null check
        
        plugin.getServer().getPluginManager().registerEvents(guiListener, plugin);
    }
    
    public GuiInventoryHolder(P plugin, int size, String title) {
        this.plugin = plugin;
        this.guiListener = new GuiListener<>(this);
        this.inventory = plugin.getServer().createInventory(this, size, title); //implicit null check
        
        plugin.getServer().getPluginManager().registerEvents(guiListener, plugin);
    }
    
    public GuiInventoryHolder(P plugin, Inventory inventory) {
        this.plugin = plugin;
        this.inventory = inventory;
        this.guiListener = new GuiListener<>(this);

        assert getInventory().getHolder() == this;        
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public P getPlugin() {
        return plugin;
    }

    public void onClose(InventoryCloseEvent event) {
    }

    public void onClick(InventoryClickEvent event) {
    }
    
    public void onOpen(InventoryOpenEvent event) {
    }
    
}
