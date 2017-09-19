package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRRepairRecipe;

import net.minecraft.server.v1_12_R1.RecipeRepair;

public class NMSRepair extends NMSShapelessRecipe<RecipeRepair> {

    public NMSRepair(RecipeRepair delegate) {
        super(delegate);
    }
    
    @Override
    protected CRRepairRecipe createBukkitRecipe() {
        return new CRRepairRecipe(this);
    }

}
