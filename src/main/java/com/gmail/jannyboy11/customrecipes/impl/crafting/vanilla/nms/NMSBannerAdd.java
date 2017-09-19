package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBannerAddPatternRecipe;

import net.minecraft.server.v1_12_R1.RecipesBanner;

public class NMSBannerAdd extends NMSShapelessRecipe<RecipesBanner.AddRecipe> {

    public NMSBannerAdd(RecipesBanner.AddRecipe delegate) {
        super(delegate);
    }

    @Override
    protected CRBannerAddPatternRecipe createBukkitRecipe() {
        return new CRBannerAddPatternRecipe(this);
    }

}
