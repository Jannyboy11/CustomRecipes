package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;

import net.minecraft.server.v1_12_R1.IRecipe;

public class CRModifiedCraftingRecipe 
        <T extends IRecipe, R extends IRecipe, 
        NT extends NMSCraftingRecipe<T>, NR extends NMSCraftingRecipe<R>,
        M extends NMSModifiedCraftingRecipe<T, R, NT, NR>>
    
        extends CRCraftingRecipe<NR, M> implements ModifiedCraftingRecipe<CraftingRecipe> {
    
    public CRModifiedCraftingRecipe(M nmsRecipe) {
        super(nmsRecipe);
    }

    @Override
    public CraftingRecipe getBaseRecipe() {
        return this.nmsRecipe.getBase().getBukkitRecipe();
    }

    @Override
    public CraftingModifier getModifier() {
        return this.nmsRecipe.getModifier().getBukkitModifier();
    }

}
