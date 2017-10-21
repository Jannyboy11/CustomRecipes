package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import java.util.function.Function;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public interface CraftingModifier<T extends CraftingRecipe, R extends CraftingRecipe> extends Function<T, R> {
    
    public R modify(T base);
    
    @Override
    public default R apply(T base) {
        return modify(base);
    }
    
}
