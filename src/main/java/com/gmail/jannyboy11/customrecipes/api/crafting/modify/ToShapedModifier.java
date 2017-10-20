package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;

public interface ToShapedModifier<T extends CraftingRecipe, R extends ShapedRecipe> extends CraftingModifier<T, R> {
    
    public R modify(ShapedRecipe shapedRecipe);

}
