package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import java.util.Set;
import java.util.UUID;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public interface WorldModifier extends CraftingModifier<CraftingRecipe, CraftingRecipe> {

    public Set<UUID> getWorlds();
    
}
