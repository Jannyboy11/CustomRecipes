package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom;

import java.util.Objects;
import java.util.function.Predicate;

import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class NBTIngredient extends CombinedIngredient {

    private final NBTTagCompound tag;

    public NBTIngredient(Predicate<? super ItemStack> basePredicate, NBTTagCompound tag) {
        super(basePredicate, itemStack -> Objects.equals(itemStack.getTag(), tag), Boolean::logicalAnd);
        this.tag = tag;
    }

    public NBTTagCompound getTag() {
        return tag;
    }
}
