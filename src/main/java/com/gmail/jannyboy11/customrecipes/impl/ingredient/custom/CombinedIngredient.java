package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom;

import java.util.function.Predicate;

import com.gmail.jannyboy11.customrecipes.util.BooleanBinaryOpterator;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CombinedIngredient extends InjectedIngredient {

    private final Predicate<? super ItemStack> p1, p2;

    public CombinedIngredient(Predicate<? super ItemStack> p1, Predicate<? super ItemStack> p2, BooleanBinaryOpterator combiner) {
        super(itemStack -> combiner.applyAsBoolean(p1.test(itemStack), p2.test(itemStack)));
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public RecipeItemStack asNMSIngredient() {
        if (p1 instanceof RecipeItemStack) {
            RecipeItemStack rp1 = (RecipeItemStack) p1;
            if (rp1.choices != null && rp1.choices.length > 0) {
                return rp1;
            }
        }

        if (p2 instanceof RecipeItemStack) {
            RecipeItemStack rp2 = (RecipeItemStack) p2;
            if (rp2.choices != null && rp2.choices.length > 0) {
                return rp2;
            }
        }

        return super.asNMSIngredient();
    }

}
