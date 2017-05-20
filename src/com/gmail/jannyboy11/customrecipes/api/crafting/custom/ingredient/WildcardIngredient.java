package com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * This ingredient will accept any ItemStack as a valid ingredient.
 * 
 * @author Jan
 */
public class WildcardIngredient implements CraftingIngredient {
	
	/**
	 * The singleton instance
	 */
	private static WildcardIngredient instance;
	
	/**
	 * The singleton getter. WildcardIngredient uses lazy instantiation.
	 * @return the singleton instance
	 */
	public static WildcardIngredient getInstance() {
		return instance == null ? instance = new WildcardIngredient() : instance;
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
