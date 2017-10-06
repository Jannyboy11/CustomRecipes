package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public interface MenuButton {

    public default <P extends Plugin, MH extends MenuHolder<P>> void onClick(MH holder, InventoryClickEvent event) {
    }
    
    public default ItemStack getIcon() {
        return null;
    }
    
}
