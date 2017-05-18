package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.List;

import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public interface CraftingRecipe {
	
	public boolean matches(CraftingInventory craftingInventory, World world);
	
	public ItemStack craftItem(CraftingInventory craftingInventory);
	
	public ItemStack getResult();
	
	public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory);

	boolean isHidden();

}
