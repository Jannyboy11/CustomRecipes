package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.util.Objects;

public class NMSNBTModifier {

    private final NBTTagCompound tag;

    public NMSNBTModifier(NBTTagCompound tag) {
        this.tag = Objects.requireNonNull(tag);
    }

//    @Override
//    protected CRNBTModifier createBukkitModifier() {
//        return new CRNBTModifier(this);
//    }

}
