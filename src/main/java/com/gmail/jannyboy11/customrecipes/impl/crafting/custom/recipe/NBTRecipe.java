package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import java.util.Collections;
import java.util.List;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class NBTRecipe extends ShapedRecipes {
	
	private final List<NBTTagCompound> ingredientTags;

	public NBTRecipe(String group, int width, int height, NonNullList<RecipeItemStack> ingredients,
			ItemStack result, List<NBTTagCompound> ingredientTags) {
		
		super(group, width, height, ingredients, result);
		this.ingredientTags = ingredientTags;
	}
	
	
	
	public List<NBTTagCompound> getIngredientTags() {
		return Collections.unmodifiableList(ingredientTags);
	}
	
}
