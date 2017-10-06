package com.gmail.jannyboy11.customrecipes.gui;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.RedirectItemButton;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class MainMenu extends MenuHolder<CustomRecipesPlugin> {
    
    private static final ItemStack CRAFTING_BUTTON = new ItemBuilder(Material.WORKBENCH).name("Crafting Recipes").build();
    private static final ItemStack FURNACE_BUTTON = new ItemBuilder(Material.FURNACE).name("Furnace Recipes").build();

    public MainMenu(CustomRecipesPlugin plugin) {
        super(plugin, InventoryType.HOPPER, "Select a recipe type");
        
        setButton(1, new RedirectItemButton(CRAFTING_BUTTON, () -> new CraftingManagerMenu(plugin).getInventory()));
        //TODO setButton(2, new RedirectItemButton(FURNACE_BUTTON, () -> new FurnaceManagerMenu(plugin).getInventory()));
    }

}
