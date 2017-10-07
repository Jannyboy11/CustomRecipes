package com.gmail.jannyboy11.customrecipes.gui.framework;

import java.util.Objects;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;

public class GuiOpenListener implements Listener {
    
    private final Plugin plugin;
    
    public GuiOpenListener(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof GuiInventoryHolder) {
            GuiInventoryHolder guiHolder = (GuiInventoryHolder) event.getInventory().getHolder();
            plugin.getServer().getPluginManager().registerEvents(guiHolder.guiListener, plugin);
            guiHolder.onOpen(event);
        }
    }

}
