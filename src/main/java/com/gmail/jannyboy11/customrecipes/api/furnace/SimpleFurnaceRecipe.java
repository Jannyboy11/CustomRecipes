package com.gmail.jannyboy11.customrecipes.api.furnace;

import java.util.Objects;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a simple unregistered furnace recipe POJO
 * 
 * @author Jan
 */
public class SimpleFurnaceRecipe implements FurnaceRecipe {
	
	private ItemStack ingredient;
	private ItemStack output;
	private float xp;
	
	public SimpleFurnaceRecipe() {
	}
	
	public SimpleFurnaceRecipe(ItemStack ingredient) {
		this.ingredient = ingredient;
	}
	
	public SimpleFurnaceRecipe(ItemStack ingredient, ItemStack result) {
		this(ingredient);
		this.output = result;
	}
	
	public SimpleFurnaceRecipe(ItemStack ingredient, ItemStack result, float xp) {
		this(ingredient, result);
		this.xp = xp;
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
		return output;
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
	public void setIngredient(ItemStack ingredient) {
		this.ingredient = ingredient;
	}
	
	/**
	 * Set the result
	 * 
	 * @param result the new result.
	 */
	@Override
	public void setResult(ItemStack result) {
		this.output = result;
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
	public SimpleFurnaceRecipe ingredient(ItemStack ingredient) {
		return new SimpleFurnaceRecipe(ingredient, output, xp);
	}
	
	/**
	 * Builder method
	 * 
	 * @param result the result of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFurnaceRecipe result(ItemStack result) {
		return new SimpleFurnaceRecipe(ingredient, output, xp);
	}
	
	/**
	 * Builder method
	 * 
	 * @param xp the experience reward of the new furnace recipe
	 * @return a new SimpleFurnaceRecipe instance
	 */
	public SimpleFurnaceRecipe xp(float xp) {
		return new SimpleFurnaceRecipe(ingredient, output, xp);
	}
	

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FurnaceRecipe)) return false;
		
		FurnaceRecipe that = (FurnaceRecipe) o;
		return Objects.equals(ingredient, that.getIngredient()) &&
				Objects.equals(output, that.getResult()) &&
				Objects.equals(Float.floatToIntBits(xp), Float.floatToIntBits(that.getXp()));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(ingredient, output, xp);
	}
	
	@Override
	public String toString() {
		return "SimpleFurnaceRecipe{" +
				"ingredient=" + ingredient +
				",output=" + output +
				",xp=" + xp +
				"}";
	}

}
