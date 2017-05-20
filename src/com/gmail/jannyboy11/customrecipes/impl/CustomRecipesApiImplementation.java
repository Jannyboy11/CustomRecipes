package com.gmail.jannyboy11.customrecipes.impl;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.CustomRecipesApi;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.*;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.server.v1_12_R1.CraftingManager;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeArmorDye;
import net.minecraft.server.v1_12_R1.RecipeBookClone;
import net.minecraft.server.v1_12_R1.RecipeFireworks;
import net.minecraft.server.v1_12_R1.RecipeMapClone;
import net.minecraft.server.v1_12_R1.RecipeMapExtend;
import net.minecraft.server.v1_12_R1.RecipeRepair;
import net.minecraft.server.v1_12_R1.RecipeShulkerBox;
import net.minecraft.server.v1_12_R1.RecipeTippedArrow;
import net.minecraft.server.v1_12_R1.RecipesBanner;
import net.minecraft.server.v1_12_R1.RecipiesShield;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
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
	public boolean addToCraftingManager(NamespacedKey key, CraftingRecipe recipe) {
		boolean wasNotCached = putRecipe(recipe);
		if (!wasNotCached) return false;
		
		MinecraftKey minecraftKey = CraftNamespacedKey.toMinecraft(key);
		IRecipe nmsRecipe = registeredCraftingRecipes.inverse().get(recipe);
		CraftingManager.a(minecraftKey, nmsRecipe);
		
		return true;
	}

	@Override
	public boolean resetRecipes() {
		clearRecipes();
		return CraftingManager.init();
	}

	@Override
	public void clearRecipes() {
		registeredCraftingRecipes.clear();
		CraftingManager.recipes = new RegistryMaterials<>();
	}
	
	private CraftingRecipe fromNMSRecipe(IRecipe recipe) {
		if (recipe == null) return null;
		return registeredCraftingRecipes.computeIfAbsent(recipe , r -> {
			//order is important.
			if (r instanceof RecipeArmorDye) {
				return new CRArmorDyeRecipe((RecipeArmorDye) r);
			} else if (r instanceof RecipesBanner.AddRecipe) {
				return new CRBannerAddPatternRecipe((RecipesBanner.AddRecipe) r);
			} else if (r instanceof RecipesBanner.DuplicateRecipe){
				return new CRBannerDuplicateRecipe((RecipesBanner.DuplicateRecipe) r);
			} else if (r instanceof RecipeBookClone) {
				return new CRBookCloneRecipe((RecipeBookClone) r);
			} else if (r instanceof RecipeFireworks) {
				return new CRFireworksRecipe((RecipeFireworks) r);
			} else if (r instanceof RecipeMapClone) {
				return new CRMapCloneRecipe((RecipeMapClone) r);
			} else if (r instanceof RecipeMapExtend) {
				return new CRMapExtendRecipe((RecipeMapExtend) r);
			} else if (r instanceof RecipeRepair) {
				return new CRRepairRecipe((RecipeRepair) r);
			} else if (r instanceof RecipiesShield.Decoration) {
				return new CRShieldDecorationRecipe((RecipiesShield.Decoration) r);
			} else if (r instanceof RecipeShulkerBox.Dye) {
				return new CRShulkerBoxDyeRecipe((RecipeShulkerBox.Dye) r);
			} else if (r instanceof RecipeTippedArrow) {
				return new CRTippedArrowRecipe((RecipeTippedArrow) r);
			} else if (r instanceof ShapedRecipes) {
				return new CRShapedRecipe<>((ShapedRecipes) r);
			} else if (r instanceof ShapelessRecipes) {
				return new CRShapelessRecipe<>((ShapelessRecipes) r);
			} else {			
				return new CRCraftingRecipe<>(r);
			}
		});
	}
	
	
	public CraftingRecipe getCraftingRecipeByKey(NamespacedKey key) {
		MinecraftKey mcKey = CraftNamespacedKey.toMinecraft(key);
		IRecipe mcRecipe = CraftingManager.a(mcKey);
		if (mcRecipe == null) return null;
		return fromNMSRecipe(mcRecipe);		
	}
	
	public boolean removeFromCraftingManager(NamespacedKey key) {
		CraftingRecipe recipe = getCraftingRecipeByKey(key);
		if (recipe == null) return false;
		IRecipe nmsRecipe = registeredCraftingRecipes.inverse().remove(recipe);
		if (nmsRecipe == null) return false;
		
		/*
		 * TODO
		 * the hard part. remove from crafting manager.
		 * 
		 * */
		
		return false;
	}
	
	public int getCraftingRecipeId(CraftingRecipe recipe) {
		IRecipe nmsRecipe = registeredCraftingRecipes.inverse().get(recipe);
		if (nmsRecipe == null) return -1;
		return CraftingManager.a(nmsRecipe);
	}

	public CraftingRecipe getById(int id) {
		return fromNMSRecipe(CraftingManager.a(id));
	}	

}
