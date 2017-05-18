package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import java.util.List;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;

public interface ShapedRecipe extends CraftingRecipe {
	
	public int getWidth();
	
	public int getHeight();
	
	public List<? extends ChoiceIngredient> getIngredients();

}
