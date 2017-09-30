package com.gmail.jannyboy11.customrecipes.gui;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class MainMenu implements InventoryHolder {

    private CustomRecipesPlugin plugin;
    private Inventory inventory;

    public MainMenu(CustomRecipesPlugin plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 9, "Manage Recipes");
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
