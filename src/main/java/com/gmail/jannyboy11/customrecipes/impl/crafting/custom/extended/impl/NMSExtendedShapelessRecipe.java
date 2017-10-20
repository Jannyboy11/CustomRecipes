package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapelessRecipe;

import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSExtendedShapelessRecipe extends NMSExtendedCraftingRecipe<ExtendedShapelessRecipe> implements ExtendedShapelessRecipe {

    public NMSExtendedShapelessRecipe(MinecraftKey key, ExtendedShapelessRecipe delegate) {
        super(key, delegate);
    }

}
