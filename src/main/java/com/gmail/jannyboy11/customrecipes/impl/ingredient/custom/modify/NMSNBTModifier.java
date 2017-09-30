package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.NBTIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.util.Objects;
import java.util.function.Predicate;

public class NMSNBTModifier extends NMSAbstractIngredientModifier<Predicate<ItemStack>, NBTIngredient> implements NBTSerializable {

    private final NBTTagCompound tag;

    public NMSNBTModifier(NBTTagCompound tag) {
        this.tag = Objects.requireNonNull(tag);
    }

    @Override
    protected CRNBTModifier createBukkitModifier() {
        return new CRNBTModifier(this);
    }

    @Override
    public NBTIngredient modify(Predicate<ItemStack> base) {
        return new NBTIngredient(base, tag);
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound serialized = new NBTTagCompound();
        serialized.set("tag", tag);
        return serialized;
    }

}
