package com.gmail.jannyboy11.customrecipes.impl.furnace;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;
import com.google.common.collect.Iterators;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.RecipesFurnace;

public class CRFurnaceManager implements FurnaceManager {
	
	public void clear() {
		clearVanilla();
		clearCustom();
	}
	
	public void clearCustom() {
		RecipesFurnace.getInstance().customRecipes.clear();
		RecipesFurnace.getInstance().customExperience.clear();
	}
	
	@Override
	public void clearVanilla() {
		RecipesFurnace.getInstance().recipes.clear();
		vanillaXp(RecipesFurnace.getInstance()).clear();
	}
	
	@Override
	public void reset() {
		ReflectionUtil.setFinalFieldValue(null, "a", new RecipesFurnace());
	}
	
	@Override
	public CRFurnaceRecipe getRecipe(org.bukkit.inventory.ItemStack bukkitStack) {
		CRFurnaceRecipe recipe = null;
		
		recipe = getCustomRecipe(bukkitStack);
		if (recipe != null) return recipe;
		
		recipe = getVanillaRecipe(bukkitStack);
		
		return recipe;
	}
	
	@Override
	public CRFurnaceRecipe getVanillaRecipe(org.bukkit.inventory.ItemStack bukkitStack) {
		return getVanillaRecipe(CraftItemStack.asNMSCopy(bukkitStack));
	}
	
	@Override
	public CRFurnaceRecipe getCustomRecipe(org.bukkit.inventory.ItemStack bukkitStack) {
		return getCustomRecipe(CraftItemStack.asNMSCopy(bukkitStack));
	}

	public CRFurnaceRecipe getRecipe(Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps, ItemStack ingedient) {
		ItemStack result = results.get(ingedient);
		return result == null ? null : new CRFurnaceRecipe(new FurnaceRecipe(results, xps, ingedient));
	}
	
	public CRFurnaceRecipe getVanillaRecipe(ItemStack nmsStack) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		return getRecipe(recipesFurnace.recipes, vanillaXp(recipesFurnace), nmsStack);
	}
	
	public CRFurnaceRecipe getCustomRecipe(ItemStack nmsStack) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		return getRecipe(recipesFurnace.customRecipes, recipesFurnace.customExperience, nmsStack);
	}

	@Override
	public Iterator<com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe> iterator() {
		return Iterators.concat(customIterator(), vanillaIterator());
	}
	
	@Override
	public Iterator<CRFurnaceRecipe> vanillaIterator() {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		
		return recipesFurnace.recipes.keySet().stream()
				.map(ingr -> new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace.recipes, vanillaXp(recipesFurnace), ingr)))
				.iterator();
	}
	
	@Override
	public Iterator<CRFurnaceRecipe> customIterator() {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		
		return recipesFurnace.recipes.keySet().stream()
				.map(ingr -> new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace.customRecipes, recipesFurnace.customExperience, ingr)))
				.iterator();
	}
	
	//TODO add methods
	
	private Map<ItemStack, Float> vanillaXp(RecipesFurnace recipesFurnace) {
		return (Map<ItemStack, Float>) ReflectionUtil.getDeclaredFieldValue(recipesFurnace, "experience");
	}

}
