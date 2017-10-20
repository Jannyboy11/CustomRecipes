package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.World;

public interface MatchStrategy<R extends ExtendedCraftingRecipe> {
    
    public boolean matches(R recipe, InventoryCrafting inventory, World world);

}
