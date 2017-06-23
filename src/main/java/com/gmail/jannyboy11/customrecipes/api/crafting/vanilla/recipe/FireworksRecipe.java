package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents the recipe responsible for modifying Fireworks.
 * @author Jan
 *
 */
public interface FireworksRecipe extends ShapelessRecipe {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {
		ItemStack stack = new ItemStack(Material.FIREWORK);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Fireworks");
		stack.setItemMeta(meta);
		return stack;
	}

}
