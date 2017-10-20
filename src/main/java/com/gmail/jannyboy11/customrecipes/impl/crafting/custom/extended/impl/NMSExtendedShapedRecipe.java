package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.Shape;

import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSExtendedShapedRecipe extends NMSExtendedCraftingRecipe<ExtendedShapedRecipe> implements ExtendedShapedRecipe {

    public NMSExtendedShapedRecipe(MinecraftKey key, ExtendedShapedRecipe delegate) {
        super(key, delegate);
    }

    @Override
    public Shape getShape() {
        return getHandle().getShape();
    }

}
