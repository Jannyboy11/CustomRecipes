package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents the recipe responsible for extending Maps.
 * @author Jan
 *
 */
public interface MapExtendRecipe extends ShapedRecipe {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {
		ItemStack stack = new ItemStack(Material.MAP);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Map Extend");
		stack.setItemMeta(meta);
		return stack;
	}

}
