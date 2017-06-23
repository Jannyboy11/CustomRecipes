package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents the recipe responsible for dying ShulkerBoxes
 * @author Jan
 *
 */
public interface ShulkerBoxDyeRecipe extends ShapelessRecipe {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {
		ItemStack stack = new ItemStack(Material.PURPLE_SHULKER_BOX);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Shulker Box Dye");
		stack.setItemMeta(meta);
		return stack;
	}

}
