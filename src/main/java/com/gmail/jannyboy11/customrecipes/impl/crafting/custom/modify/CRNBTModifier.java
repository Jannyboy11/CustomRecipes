package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class CRNBTModifier extends CRAbstractCraftingModifier<NMSNBTModifier> implements CraftingModifier, NBTSerializable {

    public CRNBTModifier(NMSNBTModifier nmsModifier) {
        super(nmsModifier);
    }

    @Override
    public CraftingRecipe modify(CraftingRecipe base) {
        //TODO

        return null;
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return nmsModifier.serializeToNbt();
    }
}
