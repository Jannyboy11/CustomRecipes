package com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.addremove;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;

public class FurnaceRemover implements BiConsumer<Player, List<String>> {
	
	private final CustomRecipesPlugin plugin;

	public FurnaceRemover(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void accept(Player player, List<String> args) {		
		ItemStack itemStack = player.getInventory().getItemInMainHand();
		if (itemStack == null) itemStack = player.getInventory().getItemInOffHand();
		if (itemStack == null) {
			player.sendMessage(ChatColor.RED + "Please use this comming while holding an item in your hand.");
			return;
		}
		
		CRFurnaceManager furnaceManager = plugin.getFurnaceManager();
		
		boolean vanilla = args.size() > 0 ? "vanilla".equalsIgnoreCase(args.get(0)) : false;
		FurnaceRecipe removed = vanilla ? furnaceManager.removeVanillaRecipe(itemStack) : furnaceManager.removeRecipe(itemStack);
		if (removed != null) {		
			player.sendMessage(ChatColor.GREEN + "Removed furnace recipe with ingredient " + 
					ChatColor.WHITE + InventoryUtils.getItemName(itemStack) +
					ChatColor.GREEN + ".");
			
			if (removed instanceof CRFixedFurnaceRecipe) {
			    plugin.disableFurnaceRecipeFile((CRFixedFurnaceRecipe) removed);
			} else {
			    //send the user an error message?
			    //a recipe has been removed, but not a fixed one.
			}
		} else {
			player.sendMessage(ChatColor.RED + "No " + (vanilla ? "vanilla" : "custom") + " furnace recipe found for ingredient " + 
					ChatColor.WHITE + InventoryUtils.getItemName(itemStack) +
					ChatColor.RED + ".");
		}
	}

}
