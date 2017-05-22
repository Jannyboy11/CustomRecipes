package com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * This ingredient will accept any ItemStack as a valid ingredient.
 * 
 * @author Jan
 */
public final class WildcardIngredient implements CraftingIngredient {
	
	/**
	 * The singleton instance
	 */
	public static final WildcardIngredient INSTANCE = new WildcardIngredient();
	
	private WildcardIngredient() {
	}

	/**
	 * Tests whether the itemstack is an ingredient.
	 * 
	 * @return true
	 */
	@Override
	public boolean isIngredient(ItemStack test) {
		return true;
	}

}
