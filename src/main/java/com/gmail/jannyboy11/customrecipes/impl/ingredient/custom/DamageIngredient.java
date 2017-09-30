package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom;

import net.minecraft.server.v1_12_R1.ItemStack;

import java.util.function.Predicate;

public class DamageIngredient extends CombinedIngredient {

    private final int damage;

    public DamageIngredient(Predicate<? super ItemStack> baseIngredient, int damage) {
        super(baseIngredient, itemStack -> itemStack.getData() == damage, Boolean::logicalAnd);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

}
