package com.gmail.jannyboy11.customrecipes.api.furnace;

import java.util.Objects;

import org.bukkit.inventory.ItemStack;

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
	
	@Override
	public ItemStack getIngredient() {
		return ingredient;
	}
	
	@Override
	public ItemStack getResult() {
		return output;
	}
	
	@Override
	public float getXp() {
		return xp;
	}
	
	@Override
	public void setIngredient(ItemStack ingredient) {
		this.ingredient = ingredient;
	}
	
	@Override
	public void setResult(ItemStack result) {
		this.output = result;
	}
	
	@Override
	public void setXp(float xp) {
		this.xp = xp;
	}
	
	public SimpleFurnaceRecipe ingredient(ItemStack ingredient) {
		return new SimpleFurnaceRecipe(ingredient, output, xp);
	}
	
	public SimpleFurnaceRecipe result(ItemStack result) {
		return new SimpleFurnaceRecipe(ingredient, output, xp);
	}
	
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

}
