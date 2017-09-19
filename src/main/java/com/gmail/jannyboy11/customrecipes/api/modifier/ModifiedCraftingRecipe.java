package com.gmail.jannyboy11.customrecipes.api.modifier;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public interface ModifiedCraftingRecipe<R extends CraftingRecipe> extends CraftingRecipe {
    
    public R getBaseRecipe();
    
    public CraftingModifier getModifier();

}
