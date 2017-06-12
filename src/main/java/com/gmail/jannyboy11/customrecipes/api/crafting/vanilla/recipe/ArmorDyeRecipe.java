package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents the Recipe responsible for dying armor.
 * 
 * @author Jan
 */
public interface ArmorDyeRecipe extends ShapelessRecipe {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {
		ItemStack stack = new ItemStack(Material.LEATHER_CHESTPLATE);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(ChatColor.GRAY + "Armor Dye");
		meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Key: " + getKey()));
		stack.setItemMeta(meta);
		return stack;
	}

}
