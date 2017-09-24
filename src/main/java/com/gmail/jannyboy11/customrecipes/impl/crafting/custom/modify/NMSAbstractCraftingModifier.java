package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;

import net.minecraft.server.v1_12_R1.IRecipe;

public abstract class NMSAbstractCraftingModifier<T extends IRecipe, R extends IRecipe> implements NMSCraftingModifier<T, R> {

    protected CraftingModifier bukkit;

    @Override
    public CraftingModifier getBukkitModifier() {
        return bukkit == null ? createBukkitModifier() : bukkit;
    }

    protected abstract CraftingModifier createBukkitModifier();

}
