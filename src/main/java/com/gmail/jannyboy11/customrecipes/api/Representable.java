package com.gmail.jannyboy11.customrecipes.api;

import org.bukkit.inventory.ItemStack;

/**
 * Indicates that instances can be represented by an ItemStack in an inventory menu.
 * 
 * @author Jan
 */
public interface Representable {
	
	/**
	 * Get the representation.
	 * 
	 * @return the representation
	 */
	public ItemStack getRepresentation();

}
