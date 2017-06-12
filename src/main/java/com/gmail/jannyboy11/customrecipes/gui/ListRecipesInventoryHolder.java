package com.gmail.jannyboy11.customrecipes.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ListRecipesInventoryHolder implements InventoryHolder {
	
	private static final int MAX_RECIPES_PER_PAGE = 9 * 5;
	
	private final List<ItemStack> recipeItems = new ArrayList<>();
	private final List<Inventory> pages = new ArrayList<>();
	private int pageNr = 0;
	
	public ListRecipesInventoryHolder(String type, Iterable<? extends ItemStack> items) {
		items.forEach(recipeItems::add);
		
		//fill page inventories
		int recipeIndex = 0;
		Inventory inventory = null;
		do {
			int inPageIndex = recipeIndex % MAX_RECIPES_PER_PAGE;
			if (inPageIndex == 0) {
				inventory = Bukkit.createInventory(this, 54, type + " recipes");
				pages.add(inventory);
			}
			
			inventory.setItem(inPageIndex, recipeItems.get(recipeIndex));
		} while (++recipeIndex < recipeItems.size());
		
		//add previous and next buttons where applicable
		for (int pageNr = 0; pageNr < pages.size(); pageNr++) {
			Inventory page = pages.get(pageNr);
			if (pageNr != 0) {
				page.setItem(45, ListRecipesListener.previousItem());
			}
			if (pageNr != pages.size() - 1) {
				page.setItem(53, ListRecipesListener.nextItem());
			}
		}
	}

	@Override
	public Inventory getInventory() {
		return pages.get(pageNr);
	}

	public void nextPage() {
		this.pageNr = Math.min(pageNr + 1, pages.size());
	}

	public void previousPage() {
		this.pageNr = Math.max(pageNr - 1, 0);
	}

}
