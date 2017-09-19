package com.gmail.jannyboy11.customrecipes.impl.modifier;

import java.util.Collections;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.modifier.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.modifier.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class CRModifiedCraftingRecipe 
        <T extends IRecipe, R extends IRecipe, 
        NT extends NMSCraftingRecipe<T>, NR extends NMSCraftingRecipe<R>,
        M extends NMSModifiedCraftingRecipe<T, R, NT, NR>> 
    
        extends CRCraftingRecipe<NR, M> implements ModifiedCraftingRecipe<CraftingRecipe> {
    
    public CRModifiedCraftingRecipe(M nmsRecipe) {
        super(nmsRecipe);
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        //TODO
        //return nmsRecipe.serializeToNbt(); ? should NMSCraftingManager implement NBTSerializable?
        return new NBTTagCompound();
    }
    
    @Override
    public Map<String, Object> serialize() {
        //TODO
        return Collections.emptyMap();
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
