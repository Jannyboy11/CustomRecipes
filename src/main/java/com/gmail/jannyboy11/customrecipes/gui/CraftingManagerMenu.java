package com.gmail.jannyboy11.customrecipes.gui;

import java.util.ArrayList;
import java.util.List;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.RedirectItemButton;

public class CraftingManagerMenu extends MenuHolder<CustomRecipesPlugin> {
    
    private static final int RECIPES_PER_PAGE = 9 * 5;
    
    private final List<CraftingRecipe> recipes = new ArrayList<>();
    private int page;

    public CraftingManagerMenu(CustomRecipesPlugin plugin) {
        super(plugin, 9 * 6, "Manage Crafting Recipes");
        
        plugin.getCraftingManager().spliterator().forEachRemaining(recipes::add);
        
        fillButtons();
    }
    
    private void fillButtons() {
        //TODO make sure page is never below zero.
        
        int listIndex = page * RECIPES_PER_PAGE;
        int inventoryIndex = 0;

        while (listIndex < recipes.size() && inventoryIndex < RECIPES_PER_PAGE) {
            CraftingRecipe recipe = recipes.get(listIndex);

            //TODO maybe keep track of the buttons in a field? for removal and such
            MenuButton button = new RedirectItemButton(recipe.getResult(), () -> new CraftingRecipeMenu(getPlugin(), recipe).getInventory());
            //TODO doesn't redirect? why not? :( it worked for the MainMenu?
            setButton(inventoryIndex, button);
            
            inventoryIndex++;
            listIndex++;
        }
    }
    
    //TODO implement next and previous buttons as well
    public void clearIcons() {
        
    }
    
}
