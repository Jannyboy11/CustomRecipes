package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.NBTModifier;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.NBTIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.util.function.Predicate;

public class CRNBTModifier extends CRAbstractIngredientModifier<Predicate<ItemStack>, NBTIngredient, NMSNBTModifier> implements NBTModifier, NBTSerializable {

    public CRNBTModifier(NMSNBTModifier nmsModifier) {
        super(nmsModifier);
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return nmsModifier.serializeToNbt();
    }
}
