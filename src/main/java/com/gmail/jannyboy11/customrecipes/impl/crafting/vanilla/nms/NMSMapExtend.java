package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapExtendRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeMapExtend;

public class NMSMapExtend extends NMSShapedRecipe<RecipeMapExtend> {

    public static final MinecraftKey KEY = new MinecraftKey("mapextending");
    
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public NMSMapExtend(RecipeMapExtend delegate) {
        super(delegate);
    }

    @Override
    protected CRMapExtendRecipe createBukkitRecipe() {
        return new CRMapExtendRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Items.FILLED_MAP);
    }
    
}
