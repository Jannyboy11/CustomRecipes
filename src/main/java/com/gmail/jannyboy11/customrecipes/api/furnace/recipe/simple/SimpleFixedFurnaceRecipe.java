package com.gmail.jannyboy11.customrecipes.api.furnace.recipe.simple;

import java.util.Map;
import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.FixedFurnaceRecipe;

/**
 * Represents a simple furnace recipe with fixed ingredient, result and experience.
 * 
 * @author Jan
 */
public final class SimpleFixedFurnaceRecipe implements FixedFurnaceRecipe {
	
    private final NamespacedKey key;
	private ItemStack ingredient;
	private ItemStack result;
	private float experience;
	
	/**
	 * Instantiates the fixed furnace recipe with the key, fixed ingredient, fixed result, and no experience.
	 * 
     * @param key the key
     * @param ingredient the fixed ingredient
     * @param result the fixed result
	 */
	public SimpleFixedFurnaceRecipe(NamespacedKey key, ItemStack ingredient, ItemStack result) {
        this.key = Objects.requireNonNull(key, "key cannot be null.");
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient cannot be null.");
        this.result = Objects.requireNonNull(result, "result cannot be null.");
    }
	
	/**
	 * Instantiates the fixed furnace recipe with the key, fixed ingredient, fixed result, and fixed experience.
	 * 
	 * @param key the key
	 * @param ingredient the fixed ingredient
	 * @param result the fixed result
	 * @param experience the fixed experience
	 */
	public SimpleFixedFurnaceRecipe(NamespacedKey key, ItemStack ingredient, ItemStack result, float experience) {
	    this(key, ingredient, result);
	    this.experience = experience;
	}
	
	/**
	 * Constructor for deserialization.
	 * 
	 * @param map the recipe in serialized form
	 */
	public SimpleFixedFurnaceRecipe(Map<String, Object> map) {
	    this.key = ((SerializableKey) map.get("key")).getKey();
		this.ingredient = (ItemStack) map.get("ingredient");
		this.result = (ItemStack) map.get("result");
		if (map.containsKey("experience")) experience = Float.valueOf(map.get("experience").toString());
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespacedKey getKey() {
        return key;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack getIngredient() {
		return ingredient;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack getResult() {
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getExperience() {
		return experience;
	}
	
	/**
	 * Set the ingredient.
	 * 
	 * @param ingredient the new ingredient
	 */
	public void setIngredient(ItemStack ingredient) {
		this.ingredient = Objects.requireNonNull(ingredient, "ingredient cannot be null");
	}
	
	/**
	 * Set the result
	 * 
	 * @param result the new result.
	 */
	public void setResult(ItemStack result) {
		this.result = Objects.requireNonNull(result, "result cannot be null");
	}
	
	/**
	 * Set the experience reward.
	 * 
	 * @param experience the new experience reward
	 */
	public void setExperience(float experience) {
		this.experience = experience;
	}
	
	/**
	 * Builder method
	 * 
	 * @param ingredient the ingredient of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFixedFurnaceRecipe ingredient(ItemStack ingredient) {
		return new SimpleFixedFurnaceRecipe(key, ingredient, result, experience);
	}
	
	/**
	 * Builder method
	 * 
	 * @param result the result of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFixedFurnaceRecipe result(ItemStack result) {
		return new SimpleFixedFurnaceRecipe(key, ingredient, result, experience);
	}
	
	/**
	 * Builder method
	 * 
	 * @param experience the experience reward of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFixedFurnaceRecipe experience(float experience) {
		return new SimpleFixedFurnaceRecipe(key, ingredient, result, experience);
	}
	

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FixedFurnaceRecipe)) return false;
		
		FixedFurnaceRecipe that = (FixedFurnaceRecipe) o;
		return Objects.equals(this.getKey(), that.getKey()) &&
		        Objects.equals(this.getIngredient(), that.getIngredient()) &&
		        Objects.equals(this.getResult(), that.getResult()) &&
		        Objects.equals(Float.floatToIntBits(this.getExperience()), Float.floatToIntBits(that.getExperience()));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getKey(), getIngredient(), getResult(), getExperience());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" +
		        "key=" + key +
				",ingredient=" + ingredient +
				",output=" + result +
				",experience=" + experience +
				"}";
	}

}
