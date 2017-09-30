package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom;

import net.minecraft.server.v1_12_R1.ItemStack;

import java.util.function.Predicate;

public class CountIngredient extends CombinedIngredient {

    private final Predicate<? super ItemStack> basePredicate;
    private final int count;

    public CountIngredient(Predicate<? super ItemStack> basePredicate, int count) {
        super(basePredicate, itemStack -> itemStack.getCount() == count, Boolean::logicalAnd);
        this.basePredicate = basePredicate;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

}
