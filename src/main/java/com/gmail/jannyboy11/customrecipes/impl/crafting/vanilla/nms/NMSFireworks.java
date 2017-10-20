package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRFireworksRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeFireworks;

public class NMSFireworks extends NMSCraftingRecipe<RecipeFireworks> {
    
    public static final MinecraftKey KEY = new MinecraftKey("fireworks");
    
    public MinecraftKey getKey() {
        return KEY;
    }
    
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
