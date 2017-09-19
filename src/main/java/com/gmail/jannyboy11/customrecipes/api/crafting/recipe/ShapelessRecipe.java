package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.List;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;

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
	 * Get the group of the recipe. Groups are used for grouping recipes together in the Recipe Book.
	 * Examples of vanilla recipes with a group are recipes that take different kinds of wood, or different colors of wool.
	 * The recipe book display mechanic is client side only, <a href="https://twitter.com/dinnerbone/status/856505341479145472">but is subject to change in Minecraft 1.13</a>.
	 * 
	 * @return the group identifier, or the empty string if the recipe has no group
	 */
	public String getGroup();

	/**
	 * Check whether this recipe has a group.
	 * 
	 * @return true if the shapeless recipe has a group, otherwise false
	 */
	public default boolean hasGroup() {
		String group = getGroup();
		return !(group == null || group.isEmpty());
	}
	
}
