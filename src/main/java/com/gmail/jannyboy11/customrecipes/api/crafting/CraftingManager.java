package com.gmail.jannyboy11.customrecipes.api.crafting;

import org.bukkit.NamespacedKey;

/**
 * Represents the crafting manager, the almighty manager of all crafting.
 * 
 * @author Jan
 *
 */
public interface CraftingManager extends Iterable<CraftingRecipe> {

	/**
	 * Register a CraftingRecipe to the crafting manager.
	 * This enables the recipe to work in crafting inventories.
	 * 
	 * @param key the key
	 * @param recipe the recipe
	 * @return whether the recipe was added successfully
	 */
	public boolean addRecipe(NamespacedKey key, CraftingRecipe recipe);

	/**
	 * Remove a CraftingRecipe from the crafting manager by its key.
	 * 
	 * @param key the key of the recipe
	 * @return the recipe if there was one registered for the key, otherwise null
	 */
	public CraftingRecipe removeRecipe(NamespacedKey key);

	/**
	 * Get a CraftingRecipe by its key.
	 * 
	 * @param key the key of the recipe
	 * @return the CraftingRecipe that has the same key, or null if no recipe with that key was found
	 */
	public CraftingRecipe getRecipe(NamespacedKey key);
	
	/**
	 * Get the key for a recipe.
	 * 
	 * @param recipe the recipe
	 * @return the key associated with the recipe, or null if the recipe was not registered
	 */
	public NamespacedKey getKey(CraftingRecipe recipe);
	
	/**
	 * Remove a recipe.
	 * 
	 * @param recipe the recipe to be removed
	 * @return the key that was associated with the recipe, or null if the recipe was not registered
	 */
	public NamespacedKey removeRecipe(CraftingRecipe recipe);

	/**
	 * Reset the crafting manager to the 'vanilla' recipes.
	 * Since vanilla recipes are now loaded from JSON files inside the server jar, this method will also load custom recipes in your server jar.
	 * 
	 * @return whether JSON files in the server jar were loaded successfully
	 */
	public boolean reset();

	/**
	 * Deletes all recipes from the crafting manager, meaning that no recipe will work anymore.
	 */
	public void clear();

}
