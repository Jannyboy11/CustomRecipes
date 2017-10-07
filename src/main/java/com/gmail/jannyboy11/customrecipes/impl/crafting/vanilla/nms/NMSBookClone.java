package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBookCloneRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.RecipeBookClone;

public class NMSBookClone extends NMSShapelessRecipe<RecipeBookClone> {

    public NMSBookClone(RecipeBookClone delegate) {
        super(delegate);
    }

    @Override
    protected CRBookCloneRecipe createBukkitRecipe() {
        return new CRBookCloneRecipe(this);
    }

    @Override
    public ItemStack b() {
        return new ItemStack(Items.WRITTEN_BOOK);
    }
    
}
