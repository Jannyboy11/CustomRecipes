package com.gmail.jannyboy11.customrecipes.impl.modify.nms;

import java.util.function.Function;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.MinecraftKey;

public interface ExtendedCraftingIngredientModifier<T extends ExtendedCraftingIngredient, R extends ExtendedCraftingIngredient> extends Function<T, R> {
    
    public R modify(T base);
    
    public default R apply(T base) {
        return modify(base);
    }

    public MinecraftKey getKey();

}
