package com.gmail.jannyboy11.customrecipes.impl.modify.nms;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class NMSNBTCraftingIngredientModifier implements ExtendedCraftingIngredientModifier<ExtendedCraftingIngredient, ExtendedCraftingIngredient> {

    public static final MinecraftKey KEY = new MinecraftKey("CustomRecipes", "crafting_nbt_modifier"); 
    
    private final NBTTagCompound tag; //can be null
    
    public NMSNBTCraftingIngredientModifier(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public ExtendedCraftingIngredient modify(ExtendedCraftingIngredient base) {
        return new ExtendedCraftingIngredientWrapper(base, input -> Objects.equals(input.getTag(), tag));
    }

    @Override
    public MinecraftKey getKey() {
        return KEY;
    }

    //TODO asItemMeta method?
}
