package com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient;

import java.util.Objects;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

public class SimilarIngredient implements CraftingIngredient {
	
	private final ItemStack itemStack;
	
	public SimilarIngredient(ItemStack itemStack) {
		this.itemStack = Objects.requireNonNull(itemStack).clone();
	}

	@Override
	public boolean isIngredient(ItemStack itemStack) {
		return this.itemStack.isSimilar(itemStack);
	}

}
