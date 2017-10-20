package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedChoiceIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class RecipeItemStackAdapter implements ExtendedChoiceIngredient {
    
    public static final RecipeItemStackAdapter EMPTY = new RecipeItemStackAdapter(RecipeItemStack.a);
    
    private final RecipeItemStack vanilla;
    
    protected RecipeItemStackAdapter(RecipeItemStack vanilla) {
        this.vanilla = Objects.requireNonNull(vanilla);
    }
    
    public static RecipeItemStackAdapter get(RecipeItemStack vanilla) {
        return vanilla == RecipeItemStack.a ? EMPTY : new RecipeItemStackAdapter(vanilla);
    }

    @Override
    public boolean accepts(ItemStack input) {
        return vanilla.a(input);
    }

    @Override
    public NonNullList<ItemStack> getChoices() {
        return vanilla.choices == null ? NonNullList.a() : NonNullList.a(ItemStack.a, vanilla.choices);
    }

}
