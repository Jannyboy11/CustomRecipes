package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRTippedArrowRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeTippedArrow;

public class NMSTippedArrow extends NMSCraftingRecipe<RecipeTippedArrow> {

    public static final MinecraftKey KEY = new MinecraftKey("tippedarrow");
    
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public NMSTippedArrow(RecipeTippedArrow delegate) {
        super(delegate);
    }

    @Override
    protected CRTippedArrowRecipe createBukkitRecipe() {
        return new CRTippedArrowRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Items.TIPPED_ARROW);
    }
}
