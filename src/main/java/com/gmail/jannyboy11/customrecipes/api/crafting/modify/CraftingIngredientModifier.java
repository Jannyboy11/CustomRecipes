package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import java.util.function.Function;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;

public interface CraftingIngredientModifier<T extends CraftingIngredient, R extends CraftingIngredient> extends Function<T, R> {
    
    public R modify(T baseIngredient);

    @Override
    public default R apply(T base) {
        return modify(base);
    }
    
}
