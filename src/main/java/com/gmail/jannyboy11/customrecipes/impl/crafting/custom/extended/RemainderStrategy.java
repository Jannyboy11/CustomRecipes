package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;

public interface RemainderStrategy<R extends ExtendedCraftingRecipe> {
    
    public NonNullList<ItemStack> getRemaining(R recipe, InventoryCrafting craftingInventory);

}
