package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a shaped recipe.
 * @author Jan
 *
 */
public interface ShapedRecipe extends CraftingRecipe, ConfigurationSerializable {
	
	/**
	 * Get the width of the recipe
	 * @return
	 */
	public int getWidth();
	
	/**
	 * Get the height of the recipe
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Get the ingredients of the recipe.
	 * The size of the list must be equal to getWidth() * getHeight();
	 * 
	 * @return the ingredients
	 */
	public List<? extends ChoiceIngredient> getIngredients();

	public default Map<String, Object> serialize() {
		return Map.of("key", new SerializableKey(getKey()),
				"ingredients", getIngredients(),
				"result", getResult(),
				"hidden", isHidden(),
				"group", getGroup(),
				"width", getWidth(),
				"height", getHeight());
	}


}
