package com.gmail.jannyboy11.customrecipes.api.util;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.modify.ModifiedFurnaceRecipe;

public class RecipeUtils {
    
    private RecipeUtils() {}

    /**
     * Gets the top level base recipe of a modified crafting recipe.
     *
     * @param recipe the modified recipe
     * @return the base recipe
     */
    public static CraftingRecipe getBase(ModifiedCraftingRecipe<?> recipe) {
        CraftingRecipe base = recipe.getBaseRecipe();
        while (base instanceof ModifiedCraftingRecipe) {
            base = ((ModifiedCraftingRecipe<?>) base).getBaseRecipe();
        }
        return base;
    }

    /**
     * Gets the top level base recipe of a modified furnace recipe.
     *
     * @param recipe the modified recipe
     * @return the base recipe
     */
    public static FurnaceRecipe getBase(ModifiedFurnaceRecipe<?> recipe) {
        FurnaceRecipe base = recipe.getBaseRecipe();
        while (base instanceof ModifiedFurnaceRecipe) {
            base = ((ModifiedFurnaceRecipe<?>) base).getBaseRecipe();
        }
        return base;
    }
    
    //TODO layoutIngredients(GridView, int offsetX, int offsetY)

}
