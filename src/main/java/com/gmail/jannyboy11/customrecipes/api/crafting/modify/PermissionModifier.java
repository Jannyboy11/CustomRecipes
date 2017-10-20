package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public interface PermissionModifier extends CraftingModifier<CraftingRecipe, CraftingRecipe> {
    
    public String getPermission();

}
