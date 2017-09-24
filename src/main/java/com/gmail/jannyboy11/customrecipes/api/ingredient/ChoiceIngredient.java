package com.gmail.jannyboy11.customrecipes.api.ingredient;

import java.util.List;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * A crafting ingredient that will accept items similar to items in the choices list.
 * 
 * @author Jan
 */
public interface ChoiceIngredient extends Ingredient, ConfigurationSerializable {
	
	/**
	 * Get the list of choices.
	 * @return the list of choices
	 */
	public List<? extends ItemStack> getChoices();

}
