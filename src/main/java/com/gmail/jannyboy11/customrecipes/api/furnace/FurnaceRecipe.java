package com.gmail.jannyboy11.customrecipes.api.furnace;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.api.RepresentableRecipe;

/**
 * Represents a furnace recipe.
 * 
 * @author Jan
 */
public interface FurnaceRecipe extends RepresentableRecipe {
	
	/**
	 * Get the ingredient of the recipe.
	 * 
	 * @return the ingredient
	 */
	public ItemStack getIngredient();
	
	/**
	 * Get the result of the recipe.
	 * 
	 * @return the result, or null if the recipe has no result
	 */
	public ItemStack getResult();
	
	/**
	 * Get the experience award for this recipe.
	 * 
	 * @return the experience reward, or 0F if the recipe has no experience reward 
	 */
	public float getXp();
	
	/**
	 * Change the ingredient of the recipe.
	 * If the recipe is registered, the updated ingredient will already work with furnaces.
	 * 
	 * @param ingredient the new ingredient of the recipe
	 */
	public void setIngredient(ItemStack ingredient);
	
	/**
	 * Change the result of the recipe.
	 * If the recipe is registered, the result recipe will be put out by furnaces.
	 * 
	 * @param result the output of the furnace
	 */
	public void setResult(ItemStack result);
	
	/**
	 * Set the experience reward of this recipe.
	 * If the recipe is registered, furnaces will give the new experience reward.
	 * 
	 * @param xp the new experience reward
	 */
	public void setXp(float xp);
	
	
	@Override
	public default ItemStack getRepresentation() {
		ItemStack result = getResult();
		ItemStack representation = result == null ? new ItemStack(Material.AIR) : result.clone();
		
		ItemMeta meta = representation.getItemMeta();
		meta.setDisplayName("");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "Ingredient: " + getIngredient().getType().name());
		lore.add(ChatColor.DARK_GRAY + "Result: " + getResult().getType().name());
		float xp = getXp(); if (xp > Float.MIN_VALUE) lore.add(ChatColor.DARK_GRAY + "Experience: " + xp);
		
		representation.setItemMeta(meta);
		return representation;
	}

}
