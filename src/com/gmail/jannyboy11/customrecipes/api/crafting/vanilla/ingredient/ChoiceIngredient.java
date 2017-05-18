package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

public interface ChoiceIngredient extends CraftingIngredient {
	
	public List<? extends ItemStack> getChoices();

}
