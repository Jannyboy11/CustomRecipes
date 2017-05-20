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
	public boolean addRecipe(NamespacedKey key, CraftingRecipe recipe);

	/*
	 * TODO document this
	 */
	public boolean removeRecipe(NamespacedKey key);

	/*
	 * TODO document this
	 */
	public CraftingRecipe getRecipe(NamespacedKey key);

	/**
	 * Reset the crafting manager to the vanilla recipes. Since vanilla recipes are now loaded from JSON files inside the server jar,
	 * This method will also load custom recipes in your server jar.
	 * 
	 * @return whether JSON files in the server jar were loaded successfully
	 */
	public boolean reset();

	/**
	 * Deletes all recipes from the crafting manager, meaning that no recipe will work anymore.
	 */
	public void clear();

}
