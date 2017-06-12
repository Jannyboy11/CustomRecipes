package com.gmail.jannyboy11.customrecipes.api;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryUtils {
	
	private InventoryUtils() {}
	
	/**
	 * Checks whether the given ItemStack is empty.
	 * 
	 * 
	 */
	public static boolean isEmptyStack(ItemStack itemStack) {
		return itemStack == null || itemStack.getType() == Material.AIR;
	}
	
	/**
	 * Get the name of an ItemStack
	 * 
	 * @param itemStack the itemStack
	 * @return the name if present, otherwise the name of the Material
	 */
	public static String getItemName(ItemStack itemStack) {
		if (itemStack == null) return Material.AIR.name();
		
		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			return itemStack.getItemMeta().getDisplayName();
		}
		
		return itemStack.getType().name();
	}
	
	/**
	 * Get 2d coordinates of an inventory slot.
	 * 
	 * @param rowLength the length of rows in the inventory
	 * @param index the slot number
	 * @return an array of two ints, the first being the row number, the second being the column number
	 */
	public static int[] inventoryRownumColnum(int rowLength, int index) {
		int rowNum = index / rowLength;
		int colNum = index - rowNum * rowLength;
		return new int[]{rowNum, colNum};
	}
	
	/**
	 * Get the inventory slot number from 2d coordinates.
	 * 
	 * @param rowLength the length of rows in the inventory
	 * @param rownumColnum an array of length 2, the first int being the row number, the second int being the column number
	 * @return the slot number
	 */
	public static int inventoryIndex(int rowLength, int[] rownumColnum) {
		int rowNum = rownumColnum[0];
		int colNum = rownumColnum[1];
		return rowNum * rowLength + colNum;
	}
	
	/**
	 * Checks whether the inventory is empty
	 * 
	 * @param inventory the inventory
	 * @return true if the inventory is empty, otherwise false
	 */
	public static boolean isEmpty(Inventory inventory) {
		return StreamSupport.stream(Spliterators.spliterator(inventory.iterator(),
				inventory.getSize(), Spliterator.SIZED), false)
			.allMatch(InventoryUtils::isEmptyStack);
	}

}
