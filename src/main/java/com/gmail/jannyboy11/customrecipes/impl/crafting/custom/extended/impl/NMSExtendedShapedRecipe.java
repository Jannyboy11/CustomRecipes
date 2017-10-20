package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.Shape;

public class NMSExtendedShapedRecipe extends NMSExtendedCraftingRecipe<ExtendedShapedRecipe> implements ExtendedShapedRecipe {

    public NMSExtendedShapedRecipe(ExtendedShapedRecipe delegate) {
        super(delegate);
    }

    @Override
    public Shape getShape() {
        return getHandle().getShape();
    }

}
