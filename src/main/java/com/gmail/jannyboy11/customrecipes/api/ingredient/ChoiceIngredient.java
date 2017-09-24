package com.gmail.jannyboy11.customrecipes.api.ingredient;

import java.util.List;
import java.util.Map;

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

	/**
	 * Serializes this ChoiceIngredient. The map contains at least the following:
     * <ul>
     *     <li>key: "choices", value: a List&lt? extends ItemStack&gt</li>
     * </ul>
     * <br>
     * Implementations may provide more entries in this map.
     *
	 * @return the map.
	 */
	public default Map<String, Object> serialize() {
		return Map.of("choices", getChoices());
	}

}
