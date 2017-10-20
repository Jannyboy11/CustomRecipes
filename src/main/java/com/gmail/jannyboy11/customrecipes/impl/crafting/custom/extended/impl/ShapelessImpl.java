package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapelessRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;

public class ShapelessImpl extends ExtendedMatchRecipe implements ExtendedShapelessRecipe {

    public ShapelessImpl(MinecraftKey key, ItemStack result, NonNullList<? extends ExtendedCraftingIngredient> ingredients, String group) {
        super(key, result, ingredients, VanillaMatchStrategy.SHAPELESS, group);
    }
    
}
