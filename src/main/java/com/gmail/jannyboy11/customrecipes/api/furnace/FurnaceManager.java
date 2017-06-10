package com.gmail.jannyboy11.customrecipes.api.furnace;

import java.util.Iterator;

import org.bukkit.inventory.ItemStack;

public interface FurnaceManager extends Iterable<FurnaceRecipe>{

	Iterator<? extends FurnaceRecipe> customIterator();

	Iterator<? extends FurnaceRecipe> vanillaIterator();

	FurnaceRecipe getCustomRecipe(ItemStack itemStack);

	FurnaceRecipe getVanillaRecipe(ItemStack itemStack);

	FurnaceRecipe getRecipe(ItemStack itemStack);

	void reset();

	void clearVanilla();

}
