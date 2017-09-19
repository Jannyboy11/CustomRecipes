package com.gmail.jannyboy11.customrecipes.api;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.modifier.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.modifier.ModifiedFurnaceRecipe;

public class RecipeUtils {
    
    private RecipeUtils() {}
    
    public static CraftingRecipe getBase(ModifiedCraftingRecipe<?> recipe) {
        CraftingRecipe base = recipe.getBaseRecipe();
        while (base instanceof ModifiedCraftingRecipe) {
            base = ((ModifiedCraftingRecipe<?>) base).getBaseRecipe();
        }
        return base;
    }
    
    public static FurnaceRecipe getBase(ModifiedFurnaceRecipe<?> recipe) {
        FurnaceRecipe base = recipe.getBaseRecipe();
        while (base instanceof ModifiedFurnaceRecipe) {
            base = ((ModifiedFurnaceRecipe<?>) base).getBaseRecipe();
        }
        return base;
    }

}
