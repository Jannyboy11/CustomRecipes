package com.gmail.jannyboy11.customrecipes.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;


public class ListRecipesInventoryHolder implements InventoryHolder, Listener {
	
	private static final int MAX_RECIPES_PER_PAGE = 9 * 5;
	private static final ItemStack NEXT = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
	private static final ItemStack PREVIOUS = new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA);
	static {
		ItemMeta nextMeta = NEXT.getItemMeta();
		nextMeta.setDisplayName("NEXT");
		NEXT.setItemMeta(nextMeta);
		
		ItemMeta previousMeta = PREVIOUS.getItemMeta();
		previousMeta.setDisplayName("PREVIOUS");
		PREVIOUS.setItemMeta(previousMeta);
	}
	
	private final List<ItemStack> recipeItems = new ArrayList<>();
	private final List<Inventory> pages = new ArrayList<>();
	private int pageNr = 0;
	
	public ListRecipesInventoryHolder(String type, Iterable<? extends ItemStack> items) {
		Bukkit.getPluginManager().registerEvents(this, CustomRecipesPlugin.getInstance());
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
				page.setItem(45, PREVIOUS);
			}
			if (pageNr != pages.size() - 1) {
				page.setItem(53, NEXT);
			}
		}
	}

	@Override
	public Inventory getInventory() {
		return pages.get(pageNr);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getView().getTopInventory().getHolder() == this) {
			event.setCancelled(true);
			
			if (NEXT.isSimilar(event.getCurrentItem())) {
				event.getWhoClicked().closeInventory();
				pageNr++;
				event.getWhoClicked().openInventory(getInventory());
			} else if (PREVIOUS.isSimilar(event.getCurrentItem())) {
				event.getWhoClicked().closeInventory();
				pageNr--;
				event.getWhoClicked().openInventory(getInventory());
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		if (event.getView().getTopInventory().getHolder() == this) {
			HandlerList.unregisterAll(this);
		}
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		if (event.getView().getTopInventory().getHolder() == this) {
			Bukkit.getPluginManager().registerEvents(this, CustomRecipesPlugin.getInstance());
		}
	}

}
