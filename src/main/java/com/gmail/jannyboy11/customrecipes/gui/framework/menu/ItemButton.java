package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.inventory.ItemStack;

public class ItemButton<MH extends MenuHolder<?>> implements MenuButton<MH> {
    
    private final ItemStack stack;
    
    public ItemButton(ItemStack stack) {
        this.stack = stack;
    }
    
    @Override
    public ItemStack getIcon() {
        return stack == null ? null : stack.clone();
    }
    
}
