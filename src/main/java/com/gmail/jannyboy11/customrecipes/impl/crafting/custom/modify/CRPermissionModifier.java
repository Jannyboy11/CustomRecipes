package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.PermissionModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;

import net.minecraft.server.v1_12_R1.IRecipe;

//TODO implements nbtSerializable?
public class CRPermissionModifier implements PermissionModifier {
    
    private final NMSPermissionModifier nmsModifier;
    
    public CRPermissionModifier(NMSPermissionModifier nms) {
        this.nmsModifier = Objects.requireNonNull(nms);
    }

    @Override
    public CraftingRecipe modify(CraftingRecipe base) {
        //TODO make a method in RecipeUtils that returns an IRecipe?
        NMSCraftingRecipe nms = RecipeUtils.getNMSRecipe(base);
        IRecipe nmsModified = nmsModifier.modify(nms);
        //TODO make it an NMSCraftingRecipe?
        //TODO return an actual recipe
        return null;
    }

    @Override
    public String getPermission() {
        return nmsModifier.getPermission();
    }

}
