package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.function.Predicate;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an ingredient for a CraftingRecipe.
 * 
 * @author Jan
 *
 */
@FunctionalInterface
public interface CraftingIngredient extends Predicate<ItemStack> {
	
	/**
	 * Tests whether an ItemStack matches as an ingredient for the associated crafting recipe.
	 * 
	 * @param itemStack the ItemSack to test
	 * @return whether the ItemStack is an ingredient
	 */
	public boolean isIngredient(ItemStack itemStack);
	
	/**
	 * Convenience method to make CraftingIngredient fit in functions that take a Predicate<ItemStack>.
	 * The default implementation delegates to {@link CraftingIngredient#isIngredient}.
	 */
	public default boolean test(ItemStack itemStack) {
		return isIngredient(itemStack);
	}

}
