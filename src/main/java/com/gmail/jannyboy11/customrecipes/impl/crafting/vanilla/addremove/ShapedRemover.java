package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;

public class ShapedRemover implements BiConsumer<Player, List<String>> {
	
	private final CustomRecipesPlugin plugin;

	public ShapedRemover(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void accept(Player player, List<String> args) {
		CRCraftingManager craftingManager = plugin.getCraftingManager();
		
		if (!args.isEmpty()) {
			
			String firstArg = args.get(0);
			NamespacedKey key = plugin.getKey(firstArg);

			CraftingRecipe recipe = craftingManager.removeRecipe(key);
			if (recipe != null) {
				player.sendMessage(ChatColor.GREEN + "Removed recipe with key " +
						ChatColor.WHITE + key +
						ChatColor.GREEN + " for item " +
						ChatColor.WHITE + InventoryUtils.getItemName(recipe.getResult()) +
						ChatColor.GREEN + ".");
			}
			
		} else {
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (InventoryUtils.isEmptyStack(itemInHand)) itemInHand = player.getInventory().getItemInOffHand();
			if (InventoryUtils.isEmptyStack(itemInHand)) {
				player.sendMessage(ChatColor.RED + "Usage: /removerecipe shaped <key>");
				player.sendMessage(ChatColor.RED + "Or '/removerecipe shaped' with an item in your hand.");
				return;
			}
			
			//loop until match
			CraftingRecipe toBeRemoved = null;
			for (CraftingRecipe recipe : craftingManager) {
				if (!(recipe instanceof ShapedRecipe)) continue;
				if (itemInHand.equals(recipe.getResult())) {
					toBeRemoved = recipe;
					break;
				}
			}
			if (toBeRemoved != null) {
				//match
				NamespacedKey key = craftingManager.removeRecipe(toBeRemoved);
				player.sendMessage(ChatColor.GREEN + "Removed recipe with key " +
						ChatColor.WHITE + key +
						ChatColor.GREEN + " for item " +
						ChatColor.WHITE + InventoryUtils.getItemName(toBeRemoved.getResult()) +
						ChatColor.GREEN + ".");
				return;
			}
			
			//no match
			player.sendMessage(ChatColor.RED + "No shaped recipe found for resultstack " +
					ChatColor.WHITE + InventoryUtils.getItemName(itemInHand) +
					ChatColor.RED + ".");
		} 

	}

}
