package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;
import net.minecraft.server.v1_12_R1.ItemStack;

import java.util.function.Predicate;

public abstract class NMSAbstractIngredientModifier<T extends Predicate<ItemStack>, R extends Predicate<ItemStack>> implements NMSIngredientModifier<T, R> {

    private IngredientModifier bukkit;

    protected abstract IngredientModifier createBukkitModifier();

    @Override
    public IngredientModifier getBukkitModifier() {
        return bukkit == null ? bukkit = createBukkitModifier() : bukkit;
    }
}
