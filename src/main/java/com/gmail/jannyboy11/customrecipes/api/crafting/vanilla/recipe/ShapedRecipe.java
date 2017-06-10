package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import java.util.List;

import org.bukkit.NamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;

/**
 * Represents a shaped recipe.
 * @author Jan
 *
 */
public interface ShapedRecipe extends CraftingRecipe {
	
	/**
	 * Get the width of the recipe
	 * @return
	 */
	public int getWidth();
	
	/**
	 * Get the height of the recipe
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Get the ingredients of the recipe.
	 * The size of the list must be equal to getWidth() * getHeight();
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
