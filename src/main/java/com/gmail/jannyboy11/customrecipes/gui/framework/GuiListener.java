package com.gmail.jannyboy11.customrecipes.gui.framework;

import java.util.Objects;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;

public class GuiListener<P extends Plugin> implements Listener {
    
    private final GuiInventoryHolder<P> guiInventoryHolder;
    
    public GuiListener(GuiInventoryHolder<P> guiInventoryHolder) {
        this.guiInventoryHolder = Objects.requireNonNull(guiInventoryHolder);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() != guiInventoryHolder) return;
         
        event.setCancelled(true);
        guiInventoryHolder.onClick(event);
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() == guiInventoryHolder)
            guiInventoryHolder.onClose(event);

        HandlerList.unregisterAll(this);
    }
    
    //TODO listen to InventoryOpenEvent to re-register the eventhandlers?

}
