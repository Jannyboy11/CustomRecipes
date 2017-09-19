package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapExtendRecipe;

import net.minecraft.server.v1_12_R1.RecipeMapExtend;

public class NMSMapExtend extends NMSShapedRecipe<RecipeMapExtend> {

    public NMSMapExtend(RecipeMapExtend delegate) {
        super(delegate);
    }

    @Override
    protected CRMapExtendRecipe createBukkitRecipe() {
        return new CRMapExtendRecipe(this);
    }
    
}
