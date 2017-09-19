package com.gmail.jannyboy11.customrecipes.impl.modifier;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.modifier.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import net.minecraft.server.v1_12_R1.IRecipe;

public class CRCraftingModifier implements CraftingModifier {
    
    private final NMSCraftingModifier nmsModifier;
    
    public CRCraftingModifier(NMSCraftingModifier nmsModifier) {
        this.nmsModifier = Objects.requireNonNull(nmsModifier);
    }

    @Override
    public CraftingRecipe modify(CraftingRecipe base) {
       IRecipe nmsRecipe = RecipeUtils.getNMSRecipe(base);
       IRecipe modified = nmsModifier.modify(nmsRecipe);
       return RecipeUtils.getBukkitRecipe(modified);
    }
    
    public NMSCraftingModifier getHandle() {
        return nmsModifier;
    }

}
