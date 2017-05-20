package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.function.Predicate;

import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface CraftingIngredient extends Predicate<ItemStack> {
	
	public boolean isIngredient(ItemStack itemStack);
	
	public default boolean test(ItemStack itemStack) {
		return isIngredient(itemStack);
	}

}
