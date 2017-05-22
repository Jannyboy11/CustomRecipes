package com.gmail.jannyboy11.customrecipes;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.jannyboy11.customrecipes.api.CustomRecipesApi;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingManager;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRVanillaRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceManager;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CustomRecipesPlugin extends JavaPlugin implements CustomRecipesApi {
	
	private final CRCraftingManager craftingManager = new CRCraftingManager();
	private final CRFurnaceManager furnaceManager = new CRFurnaceManager();
	
	@Override
	public void onLoad() {
		InjectedIngredient.inject();
	}
	
	public static CustomRecipesPlugin getInstance() {
		return JavaPlugin.getPlugin(CustomRecipesPlugin.class);
	}
	
	@Override
	public CraftingManager getCraftingManager() {
		return craftingManager;
	}
	
	@Override
	public FurnaceManager getFurnaceManager() {
		return furnaceManager;
	}

	@Override
	public boolean isVanillaRecipeType(CraftingRecipe recipe) {
		return recipe instanceof CRVanillaRecipe;
	}
	
	@Override
	public ShapedRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapedRecipe bukkitRecipe) {
		CraftShapedRecipe craftShapedRecipe = CraftShapedRecipe.fromBukkitRecipe(bukkitRecipe);
		ShapedRecipes nmsRecipe = (ShapedRecipes) ReflectionUtil.getDeclaredFieldValue(craftShapedRecipe, "recipe");
		return new CRShapedRecipe<>(nmsRecipe);
	}
	
	@Override
	public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe) {
		CraftShapelessRecipe craftShapelessRecipe = CraftShapelessRecipe.fromBukkitRecipe(bukkitRecipe);
		ShapelessRecipes nmsRecipe = (ShapelessRecipes) ReflectionUtil.getDeclaredFieldValue(craftShapelessRecipe, "recipe");
		return new CRShapelessRecipe<>(nmsRecipe);
	}
	

}
