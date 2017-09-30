package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;
import net.minecraft.server.v1_12_R1.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;

public interface NMSIngredientModifier<T extends Predicate<ItemStack>, R extends Predicate<ItemStack>> extends Function<T, R> {

    public R modify(T base);

    public IngredientModifier getBukkitModifier();

    public default R apply(T tRecipe) {
        return modify(tRecipe);
    }
}
