package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapCloneRecipe;

import net.minecraft.server.v1_12_R1.RecipeMapClone;

public class NMSMapClone extends NMSShapelessRecipe<RecipeMapClone> {

    public NMSMapClone(RecipeMapClone delegate) {
        super(delegate);
    }

    @Override
    protected CRMapCloneRecipe createBukkitRecipe() {
        return new CRMapCloneRecipe(this);
    }

}
