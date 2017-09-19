package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBannerDuplicateRecipe;

import net.minecraft.server.v1_12_R1.RecipesBanner;
import net.minecraft.server.v1_12_R1.RecipesBanner.DuplicateRecipe;

public class NMSBannerDuplicate extends NMSShapelessRecipe<RecipesBanner.DuplicateRecipe> {

    public NMSBannerDuplicate(DuplicateRecipe delegate) {
        super(delegate);
    }

    @Override
    protected CRBannerDuplicateRecipe createBukkitRecipe() {
        return new CRBannerDuplicateRecipe(this);
    }

}
