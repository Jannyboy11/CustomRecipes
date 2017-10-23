package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;

import java.util.function.Function;

import org.bukkit.Keyed;

public interface CraftingIngredientModifier<T extends CraftingIngredient, R extends CraftingIngredient> extends Keyed, Function<T, R> {

    public R modify(T base);
    
    @Override
    public default R apply(T base) {
        return modify(base);
    }
    
}
