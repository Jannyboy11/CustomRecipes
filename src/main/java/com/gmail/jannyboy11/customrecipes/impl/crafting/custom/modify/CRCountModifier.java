package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CountModifier;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.util.List;

public class CRCountModifier extends CRAbstractCraftingModifier<NMSCountModifier> implements CountModifier, NBTSerializable {

    public CRCountModifier(NMSCountModifier nmsModifier) {
        super(nmsModifier);
    }


    @Override
    public NBTTagCompound serializeToNbt() {
        return nmsModifier.serializeToNbt();
    }

    @Override
    public List<Integer> getCounts() {
        return nmsModifier.getCounts();
    }

}
