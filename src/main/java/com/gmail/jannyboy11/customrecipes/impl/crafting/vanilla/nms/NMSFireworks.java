package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRFireworksRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.RecipeFireworks;

public class NMSFireworks extends NMSShapelessRecipe<RecipeFireworks> {

    public NMSFireworks(RecipeFireworks delegate) {
        super(delegate);
    }
    
    @Override
    protected CRFireworksRecipe createBukkitRecipe() {
        return new CRFireworksRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Items.FIREWORKS);
    }

}
