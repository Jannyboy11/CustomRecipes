package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.CountModifier;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.util.function.Predicate;

public class CRCountModifier extends CRAbstractIngredientModifier<Predicate<ItemStack>, Predicate<ItemStack>, NMSCountModifier> implements CountModifier, NBTSerializable {

    public CRCountModifier(NMSCountModifier nmsModifier) {
        super(nmsModifier);
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return nmsModifier.serializeToNbt();
    }

    @Override
    public int getCount() {
        return nmsModifier.getCount();
    }

}
