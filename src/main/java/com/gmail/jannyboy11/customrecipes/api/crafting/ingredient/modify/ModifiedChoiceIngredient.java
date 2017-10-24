package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.ChoiceIngredient;

public interface ModifiedChoiceIngredient<T extends ChoiceIngredient> extends ModifiedCraftingIngredient<T>, ChoiceIngredient {
    
    @SuppressWarnings("rawtypes")
    public default Class<? extends ModifiedChoiceIngredient> getModifiedType() {
        return ModifiedChoiceIngredient.class;
    }

}
