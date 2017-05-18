package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class PermissionRecipe extends ShapedRecipes {

	private final String permission;
	
	public PermissionRecipe(String group, int width, int height, NonNullList<RecipeItemStack> ingredients, ItemStack result, String permission) {
		super(group, width, height, ingredients, result);
		this.permission = permission;
	}
	
	//UHHH //TODO! :D

}
