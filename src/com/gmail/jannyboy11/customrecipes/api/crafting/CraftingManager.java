package com.gmail.jannyboy11.customrecipes.api.crafting;

import org.bukkit.NamespacedKey;

public interface CraftingManager {

	/**
	 * Register a CraftingRecipe to the crafting manager. This enables the recipe to work in crafting inventories.
	 * 
	 * @param key
	 * @param recipe
	 * @return whether the recipe was added successfully
	 */
	public boolean addRecipe(CraftingRecipe recipe);

	/**
	 * Remove a CraftingRecipe from the crafting manager by its key.
	 * 
	 * @param key the key of the recipe
	 * @return true if a recipe with an equal key was found and removed, otherwise false
	 */
	public boolean removeRecipe(NamespacedKey key);

	/**
	 * Get a CraftingRecipe by its key.
	 * 
	 * @param key the key of the recipe
	 * @return the CraftingRecipe that has the same key, or null if no recipe with that key was found
	 */
	public CraftingRecipe getRecipe(NamespacedKey key);

	/**
	 * Reset the crafting manager to the vanilla recipes. Since vanilla recipes are now loaded from JSON files inside the server jar,
	 * this method will also load custom recipes in your server jar.
	 * 
	 * @return whether JSON files in the server jar were loaded successfully
	 */
	public boolean reset();

	/**
	 * Deletes all recipes from the crafting manager, meaning that no recipe will work anymore.
	 */
	public void clear();

}
