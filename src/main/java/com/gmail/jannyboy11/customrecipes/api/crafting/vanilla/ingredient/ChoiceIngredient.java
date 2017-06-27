package com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * A crafting ingredient that will accept items similar to items in the choices list.
 * 
 * @author Jan
 */
public interface ChoiceIngredient extends CraftingIngredient {
	
	/**
	 * Get the list of choices.
	 * @return the list of choices
	 */
	public List<? extends ItemStack> getChoices();
	
	
	/**
	 * Serialize method for ConfigurationSerializable interface.
	 * The default implementation returns a singleton map, the key is "choices" and the value is the list of ItemStacks. 
	 */
	public default Map<String, Object> serialize() {
		return Collections.singletonMap("choices", getChoices());
	}

}
