package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.PermissionModifier;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class CRPermissionModifier extends CRAbstractCraftingModifier<NMSPermissionModifier> implements PermissionModifier, NBTSerializable {
    
    public CRPermissionModifier(NMSPermissionModifier nms) {
        super(nms);
    }

    @Override
    public String getPermission() {
        return nmsModifier.getPermission();
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound serialized = new NBTTagCompound();
        serialized.setString("permission", nmsModifier.getPermission());
        return serialized;
    }

}
