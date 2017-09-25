package com.gmail.jannyboy11.customrecipes.api.ingredient;

import java.util.function.Predicate;

import org.bukkit.inventory.ItemStack;

/**
 * Represents an ingredient for a CraftingRecipe.
 * 
 * @author Jan
 */
@FunctionalInterface
public interface Ingredient extends Predicate<ItemStack> {
	
	/**
	 * Tests whether an ItemStack matches as an ingredient for the associated crafting recipe.
	 * 
	 * @param input the ItemSack to test
	 * @return whether the ItemStack is an ingredient
	 */
	public boolean isIngredient(ItemStack input);
	
	/**
	 * Convenience method to make CraftingIngredient fit in functions that take a Predicate<ItemStack>.
	 * <br>
	 * The default implementation delegates to {@link Ingredient#isIngredient}.
	 */
	public default boolean test(ItemStack itemStack) {
		return isIngredient(itemStack);
	}

}
