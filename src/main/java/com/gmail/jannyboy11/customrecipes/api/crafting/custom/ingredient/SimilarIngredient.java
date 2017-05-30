package com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient;

import java.util.Objects;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * This ingredient will accept ItemStacks that are similar to the given ItemStack using {@link org.bukkit.inventory.ItemStack#isSimilar(ItemStack)}
 * 
 * @author Jan
 *
 */
public class SimilarIngredient implements CraftingIngredient {
	
	private final ItemStack itemStack;
	
	/**
	 * Constructs the SimilarIngredient with the given ItemStack as similarity checker.
	 * 
	 * @param itemStack the ItemStack that is used for similarity checks - cannot be null
	 */
	public SimilarIngredient(ItemStack itemStack) {
		this.itemStack = Objects.requireNonNull(itemStack).clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIngredient(ItemStack itemStack) {
		return this.itemStack.isSimilar(itemStack);
	}

}
