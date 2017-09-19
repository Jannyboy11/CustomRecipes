package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.Iterator;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.*;
import com.gmail.jannyboy11.customrecipes.util.MapIterator;
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
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

/**
 * The CraftingManager implementation.
 * <br>
 * This class can be extended by other NMS plugins.
 * 
 * @author Jan
 */
public class CRCraftingManager implements com.gmail.jannyboy11.customrecipes.api.crafting.CraftingManager {

	protected CRRecipeRegistry recipeRegistry;

	public CRCraftingManager() {
		ensurePatchedRegistry();
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public boolean addRecipe(CraftingRecipe recipe) {
	    if (recipe == null) return false;

	    MinecraftKey key = CraftNamespacedKey.toMinecraft(recipe.getKey());
	    return addRecipe(key, RecipeUtils.getNMSRecipe(recipe));
    }
	
	
	public boolean addRecipe(NMSCraftingRecipe nmsRecipe) {
	    if (nmsRecipe == null) return false;
	    
	    return addRecipe(nmsRecipe.getKey(), nmsRecipe);
	}

	/**
	 * Register a recipe
	 * 
	 * @param key the key of the recipe
	 * @param nmsRecipe the Minecraft variant of the recipe
	 * @return whether the recipe was registered
	 */
	public boolean addRecipe(MinecraftKey key, IRecipe nmsRecipe) {
		if (key == null || nmsRecipe == null) return false;
		
		IRecipe existing = CraftingManager.a(key);
		if (existing != null) return false; //duplicate key
					
		CraftingManager.a(key, nmsRecipe);
		return true;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean reset() {
		clear();
		return CraftingManager.init(); //the CraftingManager#init() method can only be called when the registry is empty!
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		CraftingManager.recipes = recipeRegistry = new CRRecipeRegistry();
	}

	/**
	 * Obtain the CustomRecipes variant of a Minecraft recipe.
	 * 
	 * @param recipe the minecraft recipe
	 * @return the custom recipes mirror
	 */
	private CraftingRecipe fromNMSRecipe(IRecipe recipe) {
		if (recipe == null) return null;

        //TODO use RecipeUtils
        //TODO or should I delete this method? and move it to RecipeUtils completely?

        return null;
	}
	
	/**
	 * Obtain the Minecraft variant of a CustomRecipes CraftingRecipe.
	 * 
	 * @param craftingRecipe the recipe
	 * @return the minecraft handle or mirror
	 */
	public IRecipe toNMSRecipe(CraftingRecipe craftingRecipe) {
		if (craftingRecipe == null) return null;

		//TODO use RecipeUtils
        //TODO or should I delete this method? and move it to RecipeUtils completely?

		return null;
	}

	/**
	 * Get a Minecraft recipe by its key.
	 * 
	 * @param minecraftKey the key
	 * @return the recipe if one was registered for the key, or null if no recipe was registered for the key
	 */
	public NMSCraftingRecipe<?> getNMSRecipe(MinecraftKey minecraftKey) {
		if (minecraftKey == null) return null;
		ensurePatchedRegistry();
		
		return recipeRegistry.get(minecraftKey);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CraftingRecipe getRecipe(NamespacedKey key) {
		if (key == null) return null;
		
		MinecraftKey mcKey = CraftNamespacedKey.toMinecraft(key);
		NMSCraftingRecipe<?> mcRecipe = getNMSRecipe(mcKey);
		if (mcRecipe == null) return null;
		return fromNMSRecipe(mcRecipe);
	}
	
	/**
	 * Remove a Minecraft recipe by its key.
	 * 
	 * @param key the key
	 * @return the recipe what was registered for the key, or null if the key was not registered
	 */
	public NMSCraftingRecipe removeRecipe(MinecraftKey key) {
		ensurePatchedRegistry();

		NMSCraftingRecipe nmsRecipe = recipeRegistry.removeRecipe(key);

		return nmsRecipe;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CraftingRecipe removeRecipe(NamespacedKey key) {
		if (key == null) return null;
		
		MinecraftKey mcKey = CraftNamespacedKey.toMinecraft(key);
		NMSCraftingRecipe removed = removeRecipe(mcKey);
		if (removed == null) return null;
		
		CraftingRecipe recipe = fromNMSRecipe(removed);
		
		return recipe;
	}

	/**
	 * Get the iterator that iterates over all registered crafting recipes
	 * 
	 * @return the iterator
	 */
	@Override
	public Iterator<CraftingRecipe> iterator() {
		return new MapIterator<>(CraftingManager.recipes.iterator(), this::fromNMSRecipe);
	}

	/**
	 * Get the key for a recipe.
	 * 
	 * @param recipe the recipe
	 * @return the associated key, or null of the recipe was not registered
	 */
	public MinecraftKey getKey(IRecipe recipe) {
		if (recipe == null) return null;
		
		return CraftingManager.recipes.b(recipe);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NamespacedKey getKey(CraftingRecipe recipe) {
		if (recipe == null) return null;
		
		IRecipe nmsRecipe = toNMSRecipe(recipe);
		MinecraftKey key = getKey(nmsRecipe);
		return key == null ? null : CraftNamespacedKey.fromMinecraft(key);
	}

	/**
	 * Remove a Minecraft recipe
	 * 
	 * @param recipe the recipe
	 * @return the key that was associated with the recipe, or null if the recipe was not registered
	 */
	public MinecraftKey removeRecipe(IRecipe recipe) {
		if (recipe == null) return null;
		ensurePatchedRegistry();

		return recipeRegistry.removeRecipe(recipe);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public NamespacedKey removeRecipe(CraftingRecipe recipe) {
		if (recipe == null) return null;
		
		IRecipe nmsRecipe = toNMSRecipe(recipe);
		if (nmsRecipe == null) return null;
		
		MinecraftKey key = removeRecipe(nmsRecipe);
		if (key == null) return null;
		
		return CraftNamespacedKey.fromMinecraft(key);
	}

	/**
	 * Get the id for a recipe
	 * 
	 * @param recipe the recipe
	 * @return the id if the recipe was registered, otherwise -1
	 */
	public int getRecipeId(IRecipe recipe) {
		if (recipe == null) return -1;
		
		return CraftingManager.a(recipe);
	}
	
	/**
	 * Get the id for a crafting recipe
	 * 
	 * @param recipe the crafting recipe
	 * @return the id if the recipe is registered, otherwise -1
	 */
	//not in the api because the id system is likely to change in 1.13, and the method is not really needed since we already have the getKey() method
	public int getId(CraftingRecipe recipe) {
		if (recipe == null) return -1;
		
		IRecipe nmsRecipe = toNMSRecipe(recipe);
		if (nmsRecipe == null) return -1;
		
		return getRecipeId(nmsRecipe);
	}

	/**
	 * Get a recipe by its id
	 * @param id the id
	 * @return the recipe if one was registered for the id, otherwise null
	 */
	public IRecipe getNmsById(int id) {
		return CraftingManager.a(id);
	}
	
	/**
	 * Get the crafting recipe for its id
	 * 
	 * @param id the id 
	 * @return the crafting recipe if one was registered for this id, otherwise null
	 */
	//not in the api because the id system is likely to change in 1.13, and the method is not really needed since we already have the getKey() method
	public CraftingRecipe getById(int id) {
		IRecipe nmsRecipe = getNmsById(id);
		
		if (nmsRecipe == null) return null;
		
		return fromNMSRecipe(nmsRecipe);
	}
	
	
	/**
	 * This method is used to patch the RegistryMaterials in the CraftingManager to allow recipes to be removed.
	 * 
	 * This method must be called every time before the crafting manager tries to remove a recipe,
	 * because other plugins may set the recipe registry to the vanilla implementation by calling {@link org.bukkit.Server#resetRecipes()} or {@link org.bukkit.Server#clearRecipes()}. 
	 */
	public void ensurePatchedRegistry() {
		if (CraftingManager.recipes != recipeRegistry) {
			CraftingManager.recipes = recipeRegistry = new CRRecipeRegistry(CraftingManager.recipes);
		}
	}

}
