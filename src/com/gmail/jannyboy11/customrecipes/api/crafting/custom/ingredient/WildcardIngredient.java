package com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

public class WildcardIngredient implements CraftingIngredient {

	@Override
	public boolean isIngredient(ItemStack test) {
		return true;
	}

}
