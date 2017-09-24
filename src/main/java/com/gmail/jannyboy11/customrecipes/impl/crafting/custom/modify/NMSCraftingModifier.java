package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import java.util.function.Function;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;

import net.minecraft.server.v1_12_R1.IRecipe;

public interface NMSCraftingModifier<T extends IRecipe, R extends IRecipe> extends Function<T, R> {
    
    public R modify(T base);
    
    public CraftingModifier getBukkitModifier();
    
    public default R apply(T tRecipe) {
        R rRecipe = modify(tRecipe);
        return rRecipe;
    }

}
