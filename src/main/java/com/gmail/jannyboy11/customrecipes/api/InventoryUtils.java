package com.gmail.jannyboy11.customrecipes.api;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtils {
	
	private InventoryUtils() {}
	
	public static boolean isEmptyStack(ItemStack itemStack) {
		return itemStack == null || itemStack.getType() == Material.AIR;
	}
	
	public static String getItemName(ItemStack itemStack) {
		if (itemStack == null) return Material.AIR.name();
		
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			return itemStack.getItemMeta().getDisplayName() + ChatColor.RESET;
		}
		
		return itemStack.getType().name();
	}
	
	public static int[] inventoryRownumColnum(int rowLength, int index) {
		int rowNum = index / rowLength;
		int colNum = index - rowNum * rowLength;
		return new int[]{rowNum, colNum};
	}
	
	public static int inventoryIndex(int rowLength, int[] rownumColnum) {
		int rowNum = rownumColnum[0];
		int colNum = rownumColnum[1];
		return rowNum * rowLength + colNum;
	}
	
	public static boolean isEmpty(Inventory inventory) {
		return StreamSupport.stream(Spliterators.spliterator(inventory.iterator(),
				inventory.getSize(), Spliterator.SIZED), false)
			.allMatch(InventoryUtils::isEmptyStack);
	}

}
