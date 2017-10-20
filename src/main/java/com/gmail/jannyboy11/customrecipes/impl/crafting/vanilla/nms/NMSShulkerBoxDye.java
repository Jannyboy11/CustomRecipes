package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShulkerBoxDyeRecipe;

import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeShulkerBox;

public class NMSShulkerBoxDye extends NMSCraftingRecipe<RecipeShulkerBox.Dye> {

    public static final MinecraftKey KEY = new MinecraftKey("shulkerboxcoloring");
    
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public NMSShulkerBoxDye(RecipeShulkerBox.Dye delegate) {
        super(delegate);
    }
    
    @Override
    protected CRShulkerBoxDyeRecipe createBukkitRecipe() {
        return new CRShulkerBoxDyeRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Blocks.dv); //purple shulker box
    }

}
