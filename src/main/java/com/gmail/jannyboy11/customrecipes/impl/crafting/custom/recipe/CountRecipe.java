package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.CountIngredient;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class CountRecipe extends ShapedRecipes {

	public CountRecipe(String group, int width, int heigth, NonNullList<RecipeItemStack> ingredients, ItemStack result) {
		super(group, width, heigth, makeCountSpecific(ingredients), result);
	}
	
	public static NonNullList<RecipeItemStack> makeCountSpecific(NonNullList<RecipeItemStack> ingredients) {
		NonNullList<RecipeItemStack> result = NonNullList.a(ingredients.size(), RecipeItemStack.a);
		for (int i = 0; i < ingredients.size(); i++) {
			RecipeItemStack ingr = ingredients.get(i);
			if (ingr == RecipeItemStack.a) {
				result.set(i, RecipeItemStack.a);
				continue;
			}
			
			ItemStack[] choices = ingr.choices;
			if (choices.length == 0) {
				continue;
			}
			
			ItemStack firstChoice = choices[0];
			result.set(i, new CountIngredient(ingr, firstChoice.getCount()).asNMSIngredient());			
		}
		return result;
	}
	
	@Override
	public NonNullList<ItemStack> b(InventoryCrafting inventoryCrafting) {
		//no leftover items
		inventoryCrafting.clear();
		return NonNullList.a(inventoryCrafting.getSize(), ItemStack.a);
	}

}
