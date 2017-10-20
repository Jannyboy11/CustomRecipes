package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;

public interface CraftingIngredientModifier<T extends CraftingIngredient, R extends CraftingIngredient> {
    
    public R modify(T baseIngredient);

}
