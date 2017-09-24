package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import net.minecraft.server.v1_12_R1.IRecipe;

public class CRCraftingModifier<T extends IRecipe, R extends IRecipe> implements CraftingModifier {
    
    private final NMSCraftingModifier<T, R> nmsModifier;
    
    public CRCraftingModifier(NMSCraftingModifier<T, R> nmsModifier) {
        this.nmsModifier = Objects.requireNonNull(nmsModifier);
    }

    @Override
    public CraftingRecipe modify(CraftingRecipe base) {
       T nmsRecipe = (T) RecipeUtils.getNMSRecipe(base);
       R modified = nmsModifier.modify(nmsRecipe);
       return RecipeUtils.getBukkitRecipe(modified);
    }
    
    public NMSCraftingModifier<T, R> getHandle() {
        return nmsModifier;
    }

}
