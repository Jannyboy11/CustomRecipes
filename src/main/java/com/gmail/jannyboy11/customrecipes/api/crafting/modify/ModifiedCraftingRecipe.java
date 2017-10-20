package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public interface ModifiedCraftingRecipe<T extends CraftingRecipe> extends CraftingRecipe {
    
    public T getBaseRecipe();
    
    public <R extends CraftingRecipe> CraftingModifier<T, R> getModifier();

}
