package com.gmail.jannyboy11.customrecipes.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;

public class ListRecipesListener implements Listener {

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

	public static ItemStack nextItem() {
		return NEXT.clone();
	}

	public static ItemStack previousItem() {
		return PREVIOUS.clone();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getView().getTopInventory().getHolder() instanceof ListRecipesInventoryHolder) {
			event.setCancelled(true);
			ListRecipesInventoryHolder pageHolder = (ListRecipesInventoryHolder) event.getView().getTopInventory().getHolder();

			//https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/inventory/InventoryClickEvent.html
			//as per the docs, the close and open methods cannot be called in the same game tick.
			if (NEXT.isSimilar(event.getCurrentItem())) {
				Bukkit.getScheduler().runTask(CustomRecipesPlugin.getInstance(), () -> {
					event.getWhoClicked().closeInventory();
					pageHolder.nextPage();
					event.getWhoClicked().openInventory(pageHolder.getInventory());
				});
			} else if (PREVIOUS.isSimilar(event.getCurrentItem())) {
				Bukkit.getScheduler().runTask(CustomRecipesPlugin.getInstance(), () -> {
					event.getWhoClicked().closeInventory();
					pageHolder.previousPage();
					event.getWhoClicked().openInventory(pageHolder.getInventory());
				});
			}

		}
	}

}
