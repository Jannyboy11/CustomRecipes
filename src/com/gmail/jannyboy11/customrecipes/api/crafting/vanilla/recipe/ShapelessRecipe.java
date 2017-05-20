package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import java.util.List;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;

/**
 * Represents a shapeless recipe.
 * @author Jan
 *
 */
public interface ShapelessRecipe extends CraftingRecipe {
	
	public List<? extends ChoiceIngredient> getIngredients();

}
