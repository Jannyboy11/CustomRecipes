package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class NMSShapedRecipe<V extends ShapedRecipes> extends NMSCraftingRecipe<V> {

    public NMSShapedRecipe(V delegate) {
        super(delegate);
    }

    @Override
    public MinecraftKey getKey() {
        return delegate.key;
    }
    
    @Override
    public String getGroup() {
        return (String) ReflectionUtil.getDeclaredFieldValue(delegate, "e");
    }

    @Override
    protected CRShapedRecipe<V, ? extends NMSShapedRecipe<V>> createBukkitRecipe() {
        return new CRShapedRecipe<>(this);
    }

}
