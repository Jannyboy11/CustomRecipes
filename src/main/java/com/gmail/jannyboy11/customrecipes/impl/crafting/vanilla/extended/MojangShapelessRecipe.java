package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.extended;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

//TODO create a wrapper that implements ExtendedShapelessRecipe
public class MojangShapelessRecipe extends ShapelessRecipes implements MojangCraftingRecipe {
    
    private final String group;

    public MojangShapelessRecipe(String group, ItemStack result, NonNullList<RecipeItemStack> ingredients) {
        super(group, result, ingredients);
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
    
    @Override
    public String toString() {
        return "Mojang Shaped Recipe {"
                + "result = " + b()
                + "ingredients = " + d()
                + "group = " + getGroup()
                + "}";
    }
    
}
