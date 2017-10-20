package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.PermissionModifier;

public class CRPermissionModifier extends CRAbstractCraftingModifier<CraftingRecipe, CraftingRecipe, NMSPermissionModifier> implements PermissionModifier {
    
    public CRPermissionModifier(NMSPermissionModifier nms) {
        super(nms);
    }

    @Override
    public String getPermission() {
        return nmsModifier.getPermission();
    }

}
