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
		ReflectionUtil.setStaticFinalFieldValue(null, "a", new RecipesFurnace());
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

	public CRFurnaceRecipe getRecipe(RecipesFurnace recipesFurnace, Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps, ItemStack ingredient) {
		//has to be done this way since ItemStack doesn't override equals nor hashCode
		return results.keySet().stream()
				.filter(inMap -> furnaceEquals(recipesFurnace, inMap, ingredient))
				.findAny()
				.map(ignored -> new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace, results, xps, ingredient)))
				.orElse(null);
	}
	
	public CRFurnaceRecipe getVanillaRecipe(ItemStack nmsStack) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		return getRecipe(recipesFurnace, recipesFurnace.recipes, vanillaXp(recipesFurnace), nmsStack);
	}
	
	public CRFurnaceRecipe getCustomRecipe(ItemStack nmsStack) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		return getRecipe(recipesFurnace, recipesFurnace.customRecipes, recipesFurnace.customExperience, nmsStack);
	}

	@Override
	public Iterator<com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe> iterator() {
		return Iterators.concat(customIterator(), vanillaIterator());
	}
	
	@Override
	public Iterator<CRFurnaceRecipe> vanillaIterator() {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		
		return recipesFurnace.recipes.keySet().stream()
				.map(ingr -> new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace, recipesFurnace.recipes, vanillaXp(recipesFurnace), ingr)))
				.iterator();
	}
	
	@Override
	public Iterator<CRFurnaceRecipe> customIterator() {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		
		return recipesFurnace.customRecipes.keySet().stream()
				.map(ingr -> new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace, recipesFurnace.customRecipes, recipesFurnace.customExperience, ingr)))
				.iterator();
	}
	
	@Override
	public boolean removeRecipe(org.bukkit.inventory.ItemStack ingredient) {
		return removeCustomRecipe(ingredient) || removeVanillaRecipe(ingredient);
	}

	@Override
	public boolean removeVanillaRecipe(org.bukkit.inventory.ItemStack ingredient) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		ItemStack nmsStack = CraftItemStack.asNMSCopy(ingredient);
		
		//has to be done in this way since ItemStack doesn't override equals and hashCode.
		boolean removed = recipesFurnace.recipes.keySet().removeIf(inMap -> furnaceEquals(recipesFurnace, inMap, nmsStack));
		if (removed) vanillaXp(recipesFurnace).keySet().removeIf(inMap -> furnaceEquals(recipesFurnace, inMap, nmsStack));
		
		return removed;
	}

	@Override
	public boolean removeCustomRecipe(org.bukkit.inventory.ItemStack ingredient) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		ItemStack nmsStack = CraftItemStack.asNMSCopy(ingredient);
		
		//has to be done in this way since ItemStack doesn't override equals and hashCode.
		boolean removed = recipesFurnace.customRecipes.keySet().removeIf(inMap -> furnaceEquals(recipesFurnace, inMap, nmsStack));
		if (removed) recipesFurnace.customExperience.keySet().removeIf(inMap -> furnaceEquals(recipesFurnace, inMap, nmsStack));
		
		return removed;
	}

	@Override
	public CRFurnaceRecipe addCustomRecipe(com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe furnaceRecipe) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();

		org.bukkit.inventory.ItemStack ingredient = furnaceRecipe.getIngredient();
		if (ingredient == null) return null;
		
		return CRFurnaceRecipe.fromSimple(furnaceRecipe, recipesFurnace, recipesFurnace.customRecipes, recipesFurnace.customExperience);
	}

	@Override
	public CRFurnaceRecipe addVanillaRecipe(com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe furnaceRecipe) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();

		org.bukkit.inventory.ItemStack ingredient = furnaceRecipe.getIngredient();
		if (ingredient == null) return null;
		
		return CRFurnaceRecipe.fromSimple(furnaceRecipe, recipesFurnace, recipesFurnace.recipes, vanillaXp(recipesFurnace));
	}
	
	public static boolean furnaceEquals(RecipesFurnace recipesFurnace, ItemStack stack1, ItemStack stack2) {
		return (boolean) ReflectionUtil.invokeInstanceMethod(recipesFurnace, "a", stack1, stack2);
	}
	
	@Override
	public boolean ingredientEquals(org.bukkit.inventory.ItemStack stack1, org.bukkit.inventory.ItemStack stack2) {
		return furnaceEquals(RecipesFurnace.getInstance(), CraftItemStack.asNMSCopy(stack1), CraftItemStack.asNMSCopy(stack2));
	}
	
	
	public static Map<ItemStack, Float> vanillaXp(RecipesFurnace recipesFurnace) {
		return (Map<ItemStack, Float>) ReflectionUtil.getDeclaredFieldValue(recipesFurnace, "experience");
	}

}
