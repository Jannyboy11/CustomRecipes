package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.Shape;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class ShapedImpl extends ExtendedMatchRecipe implements ExtendedShapedRecipe {

    private final Shape shape;
    
    public ShapedImpl(MinecraftKey key, ItemStack result, Shape shape, String group) {
        super(key, result, shape.getIngredients(), VanillaMatchStrategy.SHAPED, VanillaRemainderStrategy.SHAPED, group);
        this.shape = shape;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

}
