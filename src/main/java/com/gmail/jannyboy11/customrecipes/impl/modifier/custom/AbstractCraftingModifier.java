package com.gmail.jannyboy11.customrecipes.impl.modifier.custom;

import com.gmail.jannyboy11.customrecipes.api.modifier.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.modifier.NMSCraftingModifier;

import net.minecraft.server.v1_12_R1.IRecipe;

public abstract class AbstractCraftingModifier<T extends IRecipe, R extends IRecipe> implements NMSCraftingModifier<T, R> {

    protected CraftingModifier bukkit;

    @Override
    public CraftingModifier getBukkitModifier() {
        return bukkit == null ? createBukkitModifier() : bukkit;
    }

    protected abstract CraftingModifier createBukkitModifier();

}
