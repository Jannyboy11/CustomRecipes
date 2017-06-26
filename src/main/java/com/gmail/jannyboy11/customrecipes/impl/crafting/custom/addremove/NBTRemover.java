package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.NBTRecipe;

import net.minecraft.server.v1_12_R1.CraftingManager;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NBTRemover implements BiConsumer<Player, List<String>> {
	
	private final CustomRecipesPlugin plugin;

	public NBTRemover(CustomRecipesPlugin plugin) {
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
				player.sendMessage(ChatColor.RED + "Usage: /removerecipe nbt <key>");
				player.sendMessage(ChatColor.RED + "Or '/removerecipe nbt' with an item in your hand.");
				return;
			}
			
			//loop until match
			net.minecraft.server.v1_12_R1.ItemStack nmsItemInHand = CraftItemStack.asNMSCopy(itemInHand);
			IRecipe toBeRemoved = null;
			for (Iterator<IRecipe> recipeIterator = CraftingManager.recipes.iterator(); recipeIterator.hasNext();) {
				IRecipe recipe = recipeIterator.next();
				if (!(recipe instanceof NBTRecipe)) continue;
				if (net.minecraft.server.v1_12_R1.ItemStack.fastMatches(nmsItemInHand, recipe.b()) &&
						Objects.equals(nmsItemInHand.getTag(), recipe.b().getTag())) {
					toBeRemoved = recipe;
					break;
				}
			}
			if (toBeRemoved != null) {
				//match
				MinecraftKey key = craftingManager.removeRecipe(toBeRemoved);
				player.sendMessage(ChatColor.GREEN + "Removed recipe with key " +
						ChatColor.WHITE + key +
						ChatColor.GREEN + " for item " +
						ChatColor.WHITE + InventoryUtils.getItemName(CraftItemStack.asCraftMirror(toBeRemoved.b())) +
						ChatColor.GREEN + ".");
				return;
			}
			
			//no match
			player.sendMessage(ChatColor.RED + "No nbt recipe found for resultstack " +
					ChatColor.WHITE + InventoryUtils.getItemName(itemInHand) +
					ChatColor.RED + ".");
		} 

	}

}
