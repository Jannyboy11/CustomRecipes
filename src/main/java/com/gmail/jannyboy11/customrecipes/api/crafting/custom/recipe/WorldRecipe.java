package com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe;

import java.util.UUID;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;

/**
 * Represents a shaped recipe that only works in a certain world.
 * 
 * @author Jan
 *
 */
public interface WorldRecipe extends ShapedRecipe {
	
	/**
	 * Get the unique universal identifier of the world that this recipe works in.
	 * 
	 * @return the uuid
	 */
	public UUID getWorld();

}
