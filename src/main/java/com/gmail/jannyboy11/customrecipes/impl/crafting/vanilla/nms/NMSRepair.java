package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRRepairRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeRepair;

public class NMSRepair extends NMSCraftingRecipe<RecipeRepair> {

    public static final MinecraftKey KEY = new MinecraftKey("repairitem");
    
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public NMSRepair(RecipeRepair delegate) {
        super(delegate);
    }
    
    @Override
    protected CRRepairRecipe createBukkitRecipe() {
        return new CRRepairRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        ItemStack itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
        itemStack.setData(1);
        return itemStack;
    }

}
