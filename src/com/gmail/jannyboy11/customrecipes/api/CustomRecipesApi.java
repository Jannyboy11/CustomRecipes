package com.gmail.jannyboy11.customrecipes.api;

import org.bukkit.NamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;

/**
 * The Custom Recipes application programming interface
 * @author Jan
 *
 */
public interface CustomRecipesApi {

	/**
	 * Translate a bukkit shaped recipe to a CustomRecipes shaped recipe.
	 * 
	 * @param bukkitRecipe the bukkit recipe
	 * @return the CustomRecipes variant
	 */
	public ShapedRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapedRecipe bukkitRecipe);

	/**
	 * Translate a bukkit shapeless recipe to a CustomRecipe shapeless recipe.
	 * @param bukkitRecipe the bukkit recipe
	 * @return the CustomRecipes variant
	 */
	public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe);

	/**
	 * Register a CraftingRecipe to the crafting manager. This enables the recipe to work in crafting inventories.
	 * 
	 * @param key
	 * @param recipe
	 * @return whether the recipe was added successfully
	 */
	public boolean addToCraftingManager(NamespacedKey key, CraftingRecipe recipe);

	/**
	 * Tests whether the crafting recipe is a vanilla kind.
	 * 
	 * @param recipe the recipe
	 * @return whether it's implementation is a vanilla implementation.
	 */
	public boolean isVanillaRecipeType(CraftingRecipe recipe);
	
	/**
	 * Reset the crafting manager to the vanilla recipes. Since vanilla recipes are now loaded from JSON files inside the server jar,
	 * This method will also load custom recipes in your server jar.
	 * 
	 * @return whether JSON files in the server jar were loaded successfully
	 */
	public boolean resetRecipes();
	
	/**
	 * Deletes all recipes from the crafting manager, meaning that no recipe will work anymore.
	 */
	public void clearRecipes();
	
}
