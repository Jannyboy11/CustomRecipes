package com.gmail.jannyboy11.customrecipes.api.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

//yes I actually need dis. TODO maybe factor it out to ModifiedCraftingIngredient and ModifiedFurnaceIngredient?
public interface ModifiedIngredient<I extends Ingredient> extends Ingredient {

    public I getBaseIngredient();

    public IngredientModifier getModifier(); //TODO make this generic?

}
