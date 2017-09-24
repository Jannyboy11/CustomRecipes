package com.gmail.jannyboy11.customrecipes.api.furnace.modify;

import java.util.function.UnaryOperator;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;

@FunctionalInterface
public interface FurnaceModifier extends UnaryOperator<FurnaceRecipe> {
    
    public FurnaceRecipe modify(FurnaceRecipe base);
    
    @Override
    public default FurnaceRecipe apply(FurnaceRecipe recipe) {
        return modify(recipe);
    }

}
