package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShulkerBoxDyeRecipe;

import net.minecraft.server.v1_12_R1.RecipeShulkerBox;

public class NMSShulkerBoxDye extends NMSShapelessRecipe<RecipeShulkerBox.Dye> {

    public NMSShulkerBoxDye(RecipeShulkerBox.Dye delegate) {
        super(delegate);
    }
    
    @Override
    protected CRShulkerBoxDyeRecipe createBukkitRecipe() {
        return new CRShulkerBoxDyeRecipe(this);
    }

}