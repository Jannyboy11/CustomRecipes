package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public interface CraftingModifier<T extends CraftingRecipe, R extends CraftingRecipe> {
    
    public R modify(T base);
    
}
