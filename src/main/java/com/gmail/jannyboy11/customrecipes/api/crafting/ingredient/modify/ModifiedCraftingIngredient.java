package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;

public interface ModifiedCraftingIngredient<T extends CraftingIngredient> extends CraftingIngredient {

    public T getBase();
    
    public <R extends CraftingIngredient> CraftingIngredientModifier<T, R> getModifier();
    
    @SuppressWarnings("rawtypes")
    public default Class<? extends ModifiedCraftingIngredient> getModifiedType() {
        return ModifiedCraftingIngredient.class;
    }
    
}
