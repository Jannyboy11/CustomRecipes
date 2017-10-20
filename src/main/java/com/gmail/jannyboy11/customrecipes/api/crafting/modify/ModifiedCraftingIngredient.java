package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;

public interface ModifiedCraftingIngredient<I extends CraftingIngredient> extends CraftingIngredient {
    
    public I getBaseIngredient();
    
    public <R extends CraftingIngredient> CraftingIngredientModifier<I, R> getModifier();

}
