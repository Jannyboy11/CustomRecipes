package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;

import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSExtendedCraftingRecipe<E extends ExtendedCraftingRecipe> extends NMSCraftingRecipe<E> implements ExtendedCraftingRecipe {
    
    private final MinecraftKey key;
    
    public NMSExtendedCraftingRecipe(MinecraftKey key, E delegate) {
        super(delegate);
        this.key = key;
    }

    @Override
    public MinecraftKey getKey() {
        return key;
    }

}
