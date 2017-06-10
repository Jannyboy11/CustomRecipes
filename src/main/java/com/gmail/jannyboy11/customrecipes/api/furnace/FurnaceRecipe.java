package com.gmail.jannyboy11.customrecipes.api.furnace;

import org.bukkit.inventory.ItemStack;

public interface FurnaceRecipe {
	
	public ItemStack getIngredient();
	
	public ItemStack getResult();
	
	public float getXp();
	
	public void setIngredient(ItemStack ingredient);
	
	public void setResult(ItemStack result);
	
	public void setXp(float xp);

}
