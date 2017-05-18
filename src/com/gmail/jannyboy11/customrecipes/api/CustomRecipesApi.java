package com.gmail.jannyboy11.customrecipes.api;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;

public interface CustomRecipesApi {

	public ShapedRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapedRecipe bukkitRecipe);

	public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe);

	public boolean addToCraftingManager(CraftingRecipe recipe);

	public boolean isVanillaRecipeType(CraftingRecipe recipe);
	
	
}
