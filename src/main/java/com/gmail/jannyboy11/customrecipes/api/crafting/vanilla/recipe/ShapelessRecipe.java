package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;

/**
 * Represents a shapeless recipe.
 * @author Jan
 *
 */
public interface ShapelessRecipe extends CraftingRecipe {
	
	/**
	 * Get the ingredients of the recipe.
	 * 
	 * @return the ingredients
	 */
	public List<? extends ChoiceIngredient> getIngredients();
	
	/**
	 * Get the group of the recipe. Groups are used for grouping recipes together in the Recipe Book.
	 * Examples of vanilla recipes with a group are recipes that take different kinds of wood, or different colors of wool.
	 * The recipe book display mechanic is client side only, <a href="https://twitter.com/dinnerbone/status/856505341479145472">but is subject to change in Minecraft 1.13</a>.
	 * 
	 * @return the group identifier, or the empty string if the recipe has no group
	 */
	public String getGroup();

	/**
	 * Check whether this recipe has a group.
	 * 
	 * @return true if the shapeless recipe has a group, otherwise false
	 */
	public default boolean hasGroup() {
		String group = getGroup();
		return !(group == null || group.isEmpty());
	}
	
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

		List<String> lore = new ArrayList<>();
		
		List<? extends ChoiceIngredient> ingredients = getIngredients();
		if (!ingredients.isEmpty()) {
			lore.add(ChatColor.DARK_GRAY + "Ingredients:");
			for (ChoiceIngredient ingredient : ingredients) {
				lore.add(ChatColor.DARK_GRAY + ingredient.getChoices().stream().map(InventoryUtils::getItemName)
						.collect(Collectors.joining(ChatColor.DARK_GRAY + ", ")));
			}
		}
		
		lore.add(ChatColor.DARK_GRAY + "Hidden: " + isHidden());
		if (hasGroup()) lore.add(ChatColor.DARK_GRAY + "Group: " + getGroup());
		
		meta.setLore(lore);
		
		representation.setItemMeta(meta);
		return representation;
	}

}
