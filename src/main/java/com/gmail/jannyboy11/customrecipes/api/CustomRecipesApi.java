package com.gmail.jannyboy11.customrecipes.api;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingManager;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceManager;

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
	 * Translate a bukkit shapeless recipe to a CustomRecipes shapeless recipe.
	 * 
	 * @param bukkitRecipe the bukkit recipe
	 * @return the CustomRecipes variant
	 */
	public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe);

	/**
	 * Tests whether the crafting recipe is a vanilla kind.
	 * 
	 * @param recipe the recipe
	 * @return whether it's implementation is a vanilla implementation
	 */
	public boolean isVanillaRecipeType(CraftingRecipe recipe);

	/**
	 * Get the furnace manager
	 * @return the furnace manager
	 */
	public FurnaceManager getFurnaceManager();

	/**
	 * Get the crafting manager
	 * @return the crafting manager
	 */
	public CraftingManager getCraftingManager();
	
	
}
