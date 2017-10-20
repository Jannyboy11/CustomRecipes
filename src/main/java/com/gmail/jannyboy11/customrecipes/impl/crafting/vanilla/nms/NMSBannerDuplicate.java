package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBannerDuplicateRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipesBanner;
import net.minecraft.server.v1_12_R1.RecipesBanner.DuplicateRecipe;

public class NMSBannerDuplicate extends NMSCraftingRecipe<RecipesBanner.DuplicateRecipe> {
    
    public static final MinecraftKey KEY = new MinecraftKey("bannerduplicate");
    
    @Override
    public MinecraftKey getKey() {
        return KEY;
    }

    public NMSBannerDuplicate(DuplicateRecipe delegate) {
        super(delegate);
    }

    @Override
    protected CRBannerDuplicateRecipe createBukkitRecipe() {
        return new CRBannerDuplicateRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Items.BANNER);
    }

}
