package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRArmorDyeRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.RecipeArmorDye;

public class NMSArmorDye extends NMSShapelessRecipe<RecipeArmorDye> {

    public NMSArmorDye(RecipeArmorDye delegate) {
        super(delegate);
    }

    @Override
    protected CRArmorDyeRecipe createBukkitRecipe() {
        return new CRArmorDyeRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Items.LEATHER_CHESTPLATE);
    }

}
