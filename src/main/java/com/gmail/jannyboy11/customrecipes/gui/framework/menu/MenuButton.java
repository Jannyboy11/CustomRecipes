package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface MenuButton<MH extends MenuHolder<?>> {

    public default void onClick(MH holder, InventoryClickEvent event) {
    }
    
    public default ItemStack getIcon() {
        return null;
    }
    
}
