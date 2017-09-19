package com.gmail.jannyboy11.customrecipes.api.ingredient;

import java.util.Collections;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * This ingredient will accept any ItemStack as a valid ingredient.
 * 
 * @author Jan
 */
public final class WildcardIngredient implements Ingredient, ConfigurationSerializable {
	
	private static final WildcardIngredient INSTANCE = new WildcardIngredient();
	
	private WildcardIngredient() {
	}
	
	/**
	 * Get the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static WildcardIngredient getInstance() {
		return INSTANCE;
	}
	

	/**
	 * Tests whether the ItemStack is an ingredient.
	 * 
	 * @return true
	 */
	@Override
	public boolean isIngredient(ItemStack test) {
		return true;
	}
	
	/**
	 * Deserialization method for the ConfigurationSerializable interface.
	 * @return the singleton instance
	 */
	public static WildcardIngredient deserialize(Map<String, Object> map) {
		return getInstance();
	}

	/**
	 * Serializes this wild card ingredient.
	 * @return an empty map
	 */
    @Override
    public Map<String, Object> serialize() {
        return Collections.emptyMap();
    }

}
