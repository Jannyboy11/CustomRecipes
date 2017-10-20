package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class NMSShapedRecipe<V extends ShapedRecipes> extends NMSCraftingRecipe<V> {
    
    protected int bukkitWidth, bukkitHeight;

    public NMSShapedRecipe(V delegate) {
        super(delegate);
    }

    @Override
    public MinecraftKey getKey() {
        return getHandle().key;
    }
    
    public int getWidth() {
        if (bukkitWidth != 0) return bukkitWidth;
        
        try {
            return bukkitWidth = (int) ReflectionUtil.getDeclaredFieldValue(delegate, "width");
        } catch (Throwable e) {
            return bukkitWidth = getHandle().toBukkitRecipe().getShape()[0].length();
        }
    }

    public int getHeight() {
        if (bukkitHeight != 0) return bukkitHeight;
        
        try {
            return bukkitHeight = (int) ReflectionUtil.getDeclaredFieldValue(delegate, "height");
        } catch (Throwable e) {
            return bukkitHeight = getHandle().toBukkitRecipe().getShape().length;
        }
    }
    
    @Override
    public String getGroup() {
        try {
            return (String) ReflectionUtil.getDeclaredFieldValue(delegate, "e");
        } catch (Throwable e) {
            return RecipeUtils.getGroup(getKey());
        }
    }

    @Override
    protected CRShapedRecipe<V, ? extends NMSShapedRecipe<V>> createBukkitRecipe() {
        return new CRShapedRecipe<>(this);
    }

}
