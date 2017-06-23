package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents the recipe responsible for potion effects on arrows.
 * @author Jan
 *
 */
public interface TippedArrowRecipe extends ShapedRecipe {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {
		ItemStack stack = new ItemStack(Material.TIPPED_ARROW);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Tipped Arrow");
		stack.setItemMeta(meta);
		return stack;
	}

}
