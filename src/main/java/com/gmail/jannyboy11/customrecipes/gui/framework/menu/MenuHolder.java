package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import com.gmail.jannyboy11.customrecipes.gui.framework.GuiInventoryHolder;

public class MenuHolder<P extends Plugin> extends GuiInventoryHolder<P> {
    
    private final Map<Integer, MenuButton> buttons = new HashMap<>();

    public MenuHolder(P plugin, InventoryType type, String title) {
        super(plugin, type, title);
    }
    
    public MenuHolder(P plugin, int size, String title) {
        super(plugin, size, title);
    }
    
    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != this.getInventory()) return;
        
        int slot = event.getSlot();
        MenuButton button = buttons.get(slot);
        
        if (button != null) button.onClick(this, event);
    }
    
    public void setButton(int slot, MenuButton button) {
        this.buttons.put(slot, button);
        getInventory().setItem(slot, button.getIcon());
    }
    
    public void unsetButton(int slot) {
        MenuButton button = this.buttons.remove(slot);
        if (button != null) getInventory().setItem(slot, null);
    }
    
    public void clearButtons() {
        Iterator<Integer> slotIterator = buttons.keySet().iterator();
        while (slotIterator.hasNext()) {
            int slot = slotIterator.next();
            getInventory().setItem(slot, null);
            slotIterator.remove();            
        }
    }
    
}
