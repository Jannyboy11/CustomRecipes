package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an ingredient for a CraftingRecipe.
 * 
 * @author Jan
 *
 */
@FunctionalInterface
public interface CraftingIngredient extends Predicate<ItemStack>, ConfigurationSerializable {
	
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
	
	/**
	 * Serialize method for ConfigurationSerializable interface.
	 * The default implementation returns an empty map.
	 */
	public default Map<String, Object> serialize() {
		return Collections.emptyMap();
	}

}
