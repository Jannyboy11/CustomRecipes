package com.gmail.jannyboy11.customrecipes.api.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

//yes I actually need dis.
public interface ModifiedIngredient<I extends Ingredient> extends Ingredient {

    public I getBaseIngredient();

    public IngredientModifier getModifier(); //TODO make this generic?

}
