package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * A crafting ingredient that will accept items similar to items in the choices list.
 * 
 * @author Jan
 */
public interface ChoiceIngredient extends CraftingIngredient {
	
	/**
	 * Get the list of choices
	 * @return the list of choices
	 */
	public List<? extends ItemStack> getChoices();

}
