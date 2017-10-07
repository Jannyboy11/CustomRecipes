package com.gmail.jannyboy11.customrecipes.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.gui.framework.GuiInventoryHolder;

public class CraftingRecipeEditor extends GuiInventoryHolder {
    
    private final CraftingRecipe OG_Recipe;

    public CraftingRecipeEditor(Plugin plugin, CraftingRecipe craftingRecipe) {
        super(plugin, 6 * 9, "Edit " + craftingRecipe.getKey());
        this.OG_Recipe = craftingRecipe;
    }
    
    @Override
    public void onOpen(InventoryOpenEvent event) {
        
    }
    
    @Override
    public void onClick(InventoryClickEvent event) {
        
    }

}
