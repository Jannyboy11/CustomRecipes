package com.gmail.jannyboy11.customrecipes.impl;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;

import com.gmail.jannyboy11.customrecipes.api.CustomRecipesApi;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRVanillaRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CustomRecipesApiImplementation implements CustomRecipesApi {
	
	private final BiMap<IRecipe, CraftingRecipe> registeredCraftingRecipes = HashBiMap.create();
	private final Map<String, Class<? extends CraftingRecipe>> registeredCraftingRecipeTypes = new HashMap<>();
	
	private boolean putRecipe(CraftingRecipe recipe) {
		if (recipe instanceof CRCraftingRecipe) {
			@SuppressWarnings("rawtypes")
			CRCraftingRecipe wrapper = (CRCraftingRecipe) recipe;
			return registeredCraftingRecipes.putIfAbsent(wrapper.getHandle(), wrapper) == null;
		} else {
			BiMap<CraftingRecipe, IRecipe> inverse = registeredCraftingRecipes.inverse();
			Bukkit2NMSRecipe nmsifiedRecipe = new Bukkit2NMSRecipe(recipe);
			return inverse.putIfAbsent(recipe, nmsifiedRecipe) == null;
		}
	}
	
	/*not sure about this one TODO refactor this, possibly?*/
	public boolean registerRecipeType(String name, Class<? extends CraftingRecipe> recipeType) {
		return registeredCraftingRecipeTypes.putIfAbsent(name, recipeType) == null;
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
	
	@Override
	public boolean addToCraftingManager(CraftingRecipe recipe) {
		boolean wasNotCached = putRecipe(recipe);
		if (!wasNotCached) return false;
		
		//TODO
		return false;
	}
	

}
