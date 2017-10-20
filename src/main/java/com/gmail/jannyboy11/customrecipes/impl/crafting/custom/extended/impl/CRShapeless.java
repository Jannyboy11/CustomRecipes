package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapelessRecipe;

public class CRShapeless extends CRCraftingRecipe<ExtendedShapelessRecipe, NMSExtendedShapelessRecipe> implements ShapelessRecipe {

    public CRShapeless(NMSExtendedShapelessRecipe nmsRecipe) {
        super(nmsRecipe);
    }

}
