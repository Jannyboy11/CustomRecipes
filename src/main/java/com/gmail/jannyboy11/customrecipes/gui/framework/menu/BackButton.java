package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

public class BackButton extends RedirectItemButton {

    private static final ItemStack BACK_BUTTON = new ItemBuilder(Material.WOOD_DOOR).name("Back").build();
    
    public BackButton(Supplier<? extends Inventory> to) {
        super(BACK_BUTTON, to);
    }
    
    public BackButton(String name, Supplier<? extends Inventory> to) {
        super(new ItemBuilder(Material.WOOD_DOOR).name(name).build(), to);
    }

}
