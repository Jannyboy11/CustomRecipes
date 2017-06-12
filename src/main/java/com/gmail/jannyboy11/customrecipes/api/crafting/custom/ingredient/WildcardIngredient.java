package com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * This ingredient will accept any ItemStack as a valid ingredient.
 * 
 * @author Jan
 */
public final class WildcardIngredient implements CraftingIngredient {
	
	private static final WildcardIngredient INSTANCE = new WildcardIngredient();
	
	private WildcardIngredient() {
	}
	
	/**
	 * Get the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static WildcardIngredient getInstance() {
		return INSTANCE;
	}
	

	/**
	 * Tests whether the ItemStack is an ingredient.
	 * 
	 * @return true
	 */
	@Override
	public boolean isIngredient(ItemStack test) {
		return true;
	}

}
