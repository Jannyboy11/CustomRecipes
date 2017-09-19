package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class NMSShapelessRecipe<R extends ShapelessRecipes> extends NMSCraftingRecipe<R> {

    public NMSShapelessRecipe(R delegate) {
        super(delegate);
    }

    @Override
    public MinecraftKey getKey() {
        return delegate.key;
    }
    
    @Override
    public String getGroup() {
        return (String) ReflectionUtil.getDeclaredFieldValue(delegate, "c");
    }

    @Override
    protected CRShapelessRecipe<R, ? extends NMSShapelessRecipe<R>> createBukkitRecipe() {
        return new CRShapelessRecipe<>(this);
    }

}
