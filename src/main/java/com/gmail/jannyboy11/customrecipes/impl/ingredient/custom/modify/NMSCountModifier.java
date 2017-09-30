package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.CountIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.*;

import java.util.function.Predicate;

public class NMSCountModifier extends NMSAbstractIngredientModifier<Predicate<ItemStack>, Predicate<ItemStack>> implements NBTSerializable {

    private final int count;

    public NMSCountModifier(int count) {
        this.count = count;
    }

    @Override
    public Predicate<ItemStack> modify(Predicate<ItemStack> base) {
        return new CountIngredient(base, count);
    }

    @Override
    protected IngredientModifier createBukkitModifier() {
        return new CRCountModifier(this);
    }


    public int getCount() {
        return count;
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound serialized = new NBTTagCompound();
        serialized.setInt("count", count);
        return serialized;
    }

}
