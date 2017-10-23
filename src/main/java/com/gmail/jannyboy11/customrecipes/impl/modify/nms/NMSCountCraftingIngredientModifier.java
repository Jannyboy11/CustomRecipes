package com.gmail.jannyboy11.customrecipes.impl.modify.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSCountCraftingIngredientModifier implements ExtendedCraftingIngredientModifier<ExtendedCraftingIngredient, ExtendedCraftingIngredient> {

    public static final MinecraftKey KEY = new MinecraftKey("CustomRecipes", "crafting_count_modifier");

    private final int count;
    
    public NMSCountCraftingIngredientModifier(int count) {
        this.count = count;
    }
    
    @Override
    public ExtendedCraftingIngredient modify(ExtendedCraftingIngredient base) {
        return new ExtendedCraftingIngredientWrapper(base, input -> input.getCount() == count, input -> ItemStack.a);
    }

    @Override
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public int getCount() {
        return count;
    }

}
