package com.gmail.jannyboy11.customrecipes.api.furnace;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.SerializableKey;

/**
 * Represents a simple unregistered furnace recipe POJO.
 * 
 * @author Jan
 */
public final class SimpleFurnaceRecipe implements FurnaceRecipe {
	
    private final NamespacedKey key;
	private CraftingIngredient ingredient;
	private ItemStack result;
	private float xp;
	
	public SimpleFurnaceRecipe(NamespacedKey key, CraftingIngredient ingredient, ItemStack result) {
        this.key = Objects.requireNonNull(key, "key cannot be null.");
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient cannot be null.");
        this.result = Objects.requireNonNull(result, "result cannot be null.");
    }
	
	public SimpleFurnaceRecipe(NamespacedKey key, CraftingIngredient ingredient, ItemStack result, float xp) {
	    this(key, ingredient, result);
	    this.xp = xp;
	}
	
	public SimpleFurnaceRecipe(Map<String, Object> map) {
	    this.key = ((SerializableKey) map.get("key")).getKey();
		this.ingredient = (CraftingIngredient) map.get("ingredient");
		this.result = (ItemStack) map.get("result");
		if (map.containsKey("xp")) xp = Float.valueOf(map.get("xp").toString());
	}
	
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", new SerializableKey(key));
        map.put("ingredient", ingredient);
        map.put("result", result);
        if (xp > 0F) map.put("xp", xp);
        return map;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CraftingIngredient getIngredient() {
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
	public float getXp() {
		return xp;
	}
	
	/**
	 * Set the ingredient.
	 * 
	 * @param ingredient the new ingredient
	 */
	@Override
	public void setIngredient(CraftingIngredient ingredient) {
		this.ingredient = ingredient;
	}
	
	/**
	 * Set the result
	 * 
	 * @param result the new result.
	 */
	@Override
	public void setResult(ItemStack result) {
		this.result = result;
	}
	
	/**
	 * Set the experience reward.
	 * 
	 * @param xp the new experience reward
	 */
	@Override
	public void setXp(float xp) {
		this.xp = xp;
	}
	
	/**
	 * Builder method
	 * 
	 * @param ingredient the ingredient of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFurnaceRecipe ingredient(CraftingIngredient ingredient) {
		return new SimpleFurnaceRecipe(key, ingredient, result, xp);
	}
	
	/**
	 * Builder method
	 * 
	 * @param result the result of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFurnaceRecipe result(ItemStack result) {
		return new SimpleFurnaceRecipe(key, ingredient, result, xp);
	}
	
	/**
	 * Builder method
	 * 
	 * @param xp the experience reward of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFurnaceRecipe xp(float xp) {
		return new SimpleFurnaceRecipe(key, ingredient, result, xp);
	}
	

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FurnaceRecipe)) return false;
		
		FurnaceRecipe that = (FurnaceRecipe) o;
		return this.key.equals(that.getKey());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getIngredient(), getResult(), getXp());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" +
		        "key=" + key +
				",ingredient=" + ingredient +
				",output=" + result +
				",xp=" + xp +
				"}";
	}

    @Override
    public NamespacedKey getKey() {
        return key;
    }

}
