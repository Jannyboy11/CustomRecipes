package com.gmail.jannyboy11.customrecipes.api.furnace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.api.Representable;

/**
 * Represents a furnace recipe.
 * 
 * @author Jan
 */
public interface FurnaceRecipe extends Representable, Recipe, ConfigurationSerializable {
	
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {		
		ItemStack result = getResult();
		
		ItemStack representation = (result == null || result.getType() == Material.AIR) ? new ItemStack(Material.AIR) : result.clone();
		if (representation.getType() == Material.AIR) {
			representation = new ItemStack(Material.STRUCTURE_BLOCK);
			ItemMeta meta = representation.getItemMeta();
			meta.setLore(Arrays.asList("Result: UNKNOWN"));
			representation.setItemMeta(meta);
			return representation;
		}
		
		ItemMeta meta = representation.getItemMeta();
		if (!meta.hasDisplayName()) meta.setDisplayName(ChatColor.GRAY + representation.getType().name());
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "Ingredient: " + getIngredient().getType().name());
		lore.add(ChatColor.DARK_GRAY + "Result: " + getResult().getType().name());
		float xp = getXp(); if (xp > 0F) lore.add(ChatColor.DARK_GRAY + "Experience: " + xp);
		meta.setLore(lore);
		
		representation.setItemMeta(meta);
		return representation;
	}

	/**
	 * Get whether this furnace recipe has an experience reward.
	 * 
	 * @return true if this recipe has an experience reward, otherwise false
	 */
	public default boolean hasXp() {
		return getXp() > 0;
	}
	
	
	@Override
	public default Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("ingredient", getIngredient());
		map.put("result", getResult());
		if (hasXp()) map.put("xp", getXp());
		return map;
	}

}
