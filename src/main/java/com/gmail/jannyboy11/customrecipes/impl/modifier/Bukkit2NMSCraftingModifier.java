package com.gmail.jannyboy11.customrecipes.impl.modifier;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.modifier.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import net.minecraft.server.v1_12_R1.IRecipe;

public class Bukkit2NMSCraftingModifier implements NMSCraftingModifier<IRecipe, IRecipe> {
    
    private final CraftingModifier bukkit;
    
    public Bukkit2NMSCraftingModifier(CraftingModifier bukkit) {
        this.bukkit = Objects.requireNonNull(bukkit);
    }

    @Override
    public IRecipe modify(IRecipe base) {
        CraftingRecipe bukkitBase = RecipeUtils.getBukkitRecipe(base);
        CraftingRecipe bukkitModified = bukkit.modify(bukkitBase);
        return RecipeUtils.getNMSRecipe(bukkitModified);
    }

    @Override
    public CraftingModifier getBukkitModifier() {
        return bukkit;
    }

}
