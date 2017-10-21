package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapelessRecipe;

public interface ToShapelessModifier<T extends CraftingRecipe, R extends ShapelessRecipe> extends CraftingModifier<T, R> {
    
    public R modify(ShapelessRecipe shapelessRecipe);

}