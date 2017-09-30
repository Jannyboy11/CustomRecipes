package com.gmail.jannyboy11.customrecipes.impl.ingredient;

import java.util.function.Predicate;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

public class CRIngredient<I extends Predicate<net.minecraft.server.v1_12_R1.ItemStack>> implements Ingredient {

    protected final I nmsIngredient;

    public CRIngredient(I nmsIngredient) {
        this.nmsIngredient = nmsIngredient;
    }

    @Override
    public boolean isIngredient(ItemStack itemStack) {
        return this.nmsIngredient.test(CraftItemStack.asNMSCopy(itemStack));
    }

    public I getHandle() {
        return nmsIngredient;
    }
}
