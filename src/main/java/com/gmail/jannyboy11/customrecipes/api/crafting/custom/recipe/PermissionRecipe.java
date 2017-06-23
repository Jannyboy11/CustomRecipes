package com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;

/**
 * Represents a permission recipe
 * @author Jan
 *
 */
public interface PermissionRecipe extends ShapedRecipe {
	
	/**
	 * Get the permission that is needed for this recipe to work
	 * 
	 * @return the permission string
	 */
	public String getPermission();
	
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
		lore.add(ChatColor.DARK_GRAY + "Permission: " + getPermission());
		lore.add(ChatColor.DARK_GRAY + "Width: " + getWidth());
		lore.add(ChatColor.DARK_GRAY + "Height: " + getHeight());
		
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
