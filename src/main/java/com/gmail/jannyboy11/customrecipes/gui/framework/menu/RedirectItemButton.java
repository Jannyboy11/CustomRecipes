package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import java.util.Objects;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Supplier;

public class RedirectItemButton extends ItemButton implements RedirectButton {
    
    private final Supplier<? extends Inventory> redirect;
    
    public RedirectItemButton(ItemStack icon, Supplier<? extends Inventory> redirect) {
        super(icon);
        this.redirect = Objects.requireNonNull(redirect);
    }

    @Override
    public Inventory to() {
        return redirect.get();
    }
    
    @Override
    public void onClick(MenuHolder holder, InventoryClickEvent event) {
        RedirectButton.super.onClick(holder, event);
    }
    
}
