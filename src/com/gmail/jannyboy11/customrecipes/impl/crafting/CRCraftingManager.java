package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.Objects;
import java.util.function.Predicate;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRArmorDyeRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBannerAddPatternRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBannerDuplicateRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBookCloneRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRFireworksRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapCloneRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapExtendRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRRepairRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShieldDecorationRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShulkerBoxDyeRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRTippedArrowRecipe;
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

public class CRCraftingManager implements com.gmail.jannyboy11.customrecipes.api.crafting.CraftingManager {
	
	protected final BiMap<IRecipe, CraftingRecipe> registeredCraftingRecipes = HashBiMap.create();
	protected final BiMap<String, Class<? extends IRecipe>> customImplementations = HashBiMap.create();
	
	protected CRRecipeRegistry limiter;
	
	public CRCraftingManager() {
		CraftingManager.recipes = this.limiter = new CRRecipeRegistry(CraftingManager.recipes);
	}
	
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
	
	// for NMS developers
	public boolean registerCustomImplementation(String recipeType, Class<? extends IRecipe> implementation) {
		return customImplementations.putIfAbsent(recipeType, implementation) == null;
	}
	
	
	@Override
	public boolean addRecipe(CraftingRecipe recipe) {
		boolean wasNotCached = putRecipe(recipe);
		if (!wasNotCached) return false;
		
		MinecraftKey minecraftKey = CraftNamespacedKey.toMinecraft(recipe.getKey());
		IRecipe nmsRecipe = registeredCraftingRecipes.inverse().get(recipe);
		CraftingManager.a(minecraftKey, nmsRecipe);
		
		return true;
	}

	@Override
	public boolean reset() {
		clear();
		return CraftingManager.init();
	}

	@Override
	public void clear() {
		registeredCraftingRecipes.clear();
		CraftingManager.recipes = new RegistryMaterials<>();
	}
	
	/*
	 * This method could be private.
	 * But I like other NMS developers to override it in their plugins if that's desired.
	 */
	public CraftingRecipe fromNMSRecipe(IRecipe recipe) {
		if (recipe == null) return null;
		return registeredCraftingRecipes.computeIfAbsent(recipe , r -> {
			//check if the recipe class was a custom implementation
			if (customImplementations.containsValue(r.getClass())) {
				//fall back to generic CRCrafting recipe if the recipe was not registered.
				return registeredCraftingRecipes.getOrDefault(r, new CRCraftingRecipe<>(r));
			}
			
			//if not custom, check which vanilla implementation. order is important.
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
			} 
			
			//unknown type of recipe.
			//best we can do is return a generic crafting recipe 
			return new CRCraftingRecipe<>(r);
		});
	}
	
	@Override
	public CraftingRecipe getRecipe(NamespacedKey key) {
		MinecraftKey mcKey = CraftNamespacedKey.toMinecraft(key);
		IRecipe mcRecipe = CraftingManager.a(mcKey);
		if (mcRecipe == null) return null;
		return fromNMSRecipe(mcRecipe);
	}
	
	@Override
	public boolean removeRecipe(NamespacedKey key) {
		CraftingRecipe recipe = getRecipe(key);
		if (recipe == null) return false;
		IRecipe nmsRecipe = registeredCraftingRecipes.inverse().remove(recipe);
		if (nmsRecipe == null) return false;
		return removeRecipe(nmsRecipe);
	}
	
	private boolean removeRecipe(IRecipe recipe) {
		return removeRecipe(p -> Objects.equals(recipe, p));
	}
	
	private boolean removeRecipe(Predicate<? super IRecipe> predicate) {
		ensureLimited();
		
		return limiter.disableRecipe(predicate);
	}
	
	private void ensureLimited() {
		if (CraftingManager.recipes != limiter) {
			CraftingManager.recipes = limiter = new CRRecipeRegistry(CraftingManager.recipes);
		}
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
