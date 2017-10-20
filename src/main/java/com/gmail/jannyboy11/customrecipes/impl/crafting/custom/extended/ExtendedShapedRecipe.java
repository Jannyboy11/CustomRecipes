package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import net.minecraft.server.v1_12_R1.NonNullList;

public interface ExtendedShapedRecipe extends ExtendedCraftingRecipe {
    
    public Shape getShape();
    
    @Override
    public default NonNullList<? extends ExtendedCraftingIngredient> getIngredients() {
        return getShape().getIngredients();
    }

}
