package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import java.util.List;

import org.bukkit.NamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;

/**
 * Represents a shapeless recipe.
 * @author Jan
 *
 */
public interface ShapelessRecipe extends CraftingRecipe {
	
	/**
	 * Get the ingredients of the recipe.
	 * 
	 * @return the ingredients
	 */
	public List<? extends ChoiceIngredient> getIngredients();
	
	/**
	 * Get the key of the recipe.
	 * 
	 * @return the key
	 */
	public NamespacedKey getKey();

}
