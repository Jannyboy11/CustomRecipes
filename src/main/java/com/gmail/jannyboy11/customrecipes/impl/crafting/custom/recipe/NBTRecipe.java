package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import com.gmail.jannyboy11.customrecipes.impl.ingredient.NBTIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class NBTRecipe extends ShapedRecipes {

	public NBTRecipe(String group, int width, int height, NonNullList<RecipeItemStack> ingredients, ItemStack result) {
		super(group, width, height, makeNbtSpecific(ingredients), result);
	}
	
	public static NonNullList<RecipeItemStack> makeNbtSpecific(NonNullList<RecipeItemStack> ingredients) {
		NonNullList<RecipeItemStack> result = NonNullList.a(ingredients.size(), RecipeItemStack.a);
		for (int i = 0; i < ingredients.size(); i++) {
			RecipeItemStack ingr = ingredients.get(i);
			if (ingr == RecipeItemStack.a) {
				result.set(i, RecipeItemStack.a);
				continue;
			}
			
			ItemStack[] choices = ingr.choices;
			if (choices.length == 0) {
				result.set(i, new NBTIngredient(ingr, null).asNMSIngredient());
				continue;
			}
			
			ItemStack firstChoice = choices[0];
			result.set(i, new NBTIngredient(ingr, firstChoice.getTag()).asNMSIngredient());			
		}
		return result;
	}
	
}
