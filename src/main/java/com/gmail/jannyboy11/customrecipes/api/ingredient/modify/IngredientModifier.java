package com.gmail.jannyboy11.customrecipes.api.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IngredientModifier extends UnaryOperator<Ingredient> {

    public Ingredient modify(Ingredient baseIngredient);

    @Override
    public default Ingredient apply(Ingredient t) {
        return modify(t);
    }

}
