package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRPermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRWorldRecipe;

public class WorldRemover implements BiConsumer<Player, List<String>> {

	private final CustomRecipesPlugin plugin;

	public WorldRemover(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void accept(Player player, List<String> args) {
		CRCraftingManager craftingManager = plugin.getCraftingManager();

		CRWorldRecipe toBeRemoved = null;

		if (!args.isEmpty()) {

			String firstArg = args.get(0);
			NamespacedKey key = plugin.getKey(firstArg);

			CraftingRecipe recipe = craftingManager.getRecipe(key);
			
			if (recipe instanceof CRWorldRecipe) {
				toBeRemoved = (CRWorldRecipe) recipe;
			} else {
				player.sendMessage(ChatColor.RED + "Couldn't remove recipe for key " + ChatColor.WHITE + key + ChatColor.RED + ", it is not a native world recipe.");
				return;
			}

		} else {
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (InventoryUtils.isEmptyStack(itemInHand)) itemInHand = player.getInventory().getItemInOffHand();
			if (InventoryUtils.isEmptyStack(itemInHand)) {
				player.sendMessage(ChatColor.RED + "Usage: /removerecipe world <key>");
				player.sendMessage(ChatColor.RED + "Or '/removerecipe world' with an item in your hand.");
				return;
			}

			//loop until match
			for (CraftingRecipe recipe : craftingManager) {
				if (!(recipe instanceof CRPermissionRecipe)) continue;
				if (itemInHand.equals(recipe.getResult())) {
					toBeRemoved = (CRWorldRecipe) recipe;
					break;
				}
			}

			//no match
			if (toBeRemoved == null) {
				player.sendMessage(ChatColor.RED + "No permssion recipe found for resultstack " +
						ChatColor.WHITE + InventoryUtils.getItemName(itemInHand) +
						ChatColor.RED + ".");
				return;
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
			plugin.disableCraftingRecipeFile("world", toBeRemoved);
		}
	}

}
