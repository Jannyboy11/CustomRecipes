package com.gmail.jannyboy11.customrecipes.api.ingredient.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

import java.util.function.UnaryOperator;

import org.bukkit.Keyed;

public interface IngredientModifier extends Keyed, UnaryOperator<Ingredient> {

    public Ingredient modify(Ingredient baseIngredient);

    @Override
    public default Ingredient apply(Ingredient t) {
        return modify(t);
    }

}
