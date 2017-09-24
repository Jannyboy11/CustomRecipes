package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import java.util.function.UnaryOperator;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

@FunctionalInterface
public interface CraftingModifier extends UnaryOperator<CraftingRecipe> {
    
    public CraftingRecipe modify(CraftingRecipe base);
    
    @Override
    public default CraftingRecipe apply(CraftingRecipe recipe) {
        return modify(recipe);
    }

}
