package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.inventory.ItemStack;

public class ItemButton implements MenuButton {
    
    private final ItemStack stack;
    
    public ItemButton(ItemStack stack) {
        this.stack = stack;
    }
    
    @Override
    public ItemStack getIcon() {
        return stack == null ? null : stack.clone();
    }

}
