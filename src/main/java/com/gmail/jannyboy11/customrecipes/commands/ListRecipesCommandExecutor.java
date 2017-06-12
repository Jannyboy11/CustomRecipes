package com.gmail.jannyboy11.customrecipes.commands;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.gui.ListRecipesInventoryHolder;

public class ListRecipesCommandExecutor implements CommandExecutor {
	
	private final Function<String, ? extends List<? extends Recipe>> recipesByTypeMapper;
	private final Map<String, Function<? super Recipe, ? extends ItemStack>> recipeToItemMap;
	private final Map<String, BiConsumer<? super Recipe, ? super CommandSender>> recipeToCommandSenderDiplayMap;
	
	public ListRecipesCommandExecutor(Function<String, ? extends List<? extends Recipe>> recipesByTypeMapper,
			Map<String, Function<? super Recipe, ? extends ItemStack>> recipeToItemMap,
			Map<String, BiConsumer<? super Recipe, ? super CommandSender>> recipeToCommandSenderDiplayMap) {
		this.recipesByTypeMapper = recipesByTypeMapper;
		this.recipeToItemMap = recipeToItemMap;
		this.recipeToCommandSenderDiplayMap = recipeToCommandSenderDiplayMap;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) return false;
		
		String recipeType = args[0];
		List<? extends Recipe> recipes = recipesByTypeMapper.apply(recipeType);
		
		if (recipes == null) {
			sender.sendMessage(ChatColor.RED + "Unknown recipe type: " + recipeType);
			return true;
		} else if (recipes.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "No recipes found for type: " + recipeType);
			return true;
		}
		
		return sender instanceof Player ? listPlayer((Player) sender, recipeType, recipes) : listSender(sender, recipeType, recipes);
	}
	
	
	private boolean listPlayer(Player player, String recipeType, List<? extends Recipe> recipes) {
		Function<? super Recipe, ? extends ItemStack> representationFunction = recipeToItemMap.get(recipeType);
		if (representationFunction == null) {
			player.sendMessage(ChatColor.RED + "No representation function found for this type of recipe. Trying chat messages..");
			return listSender(player, recipeType, recipes);
		}
		
		List<? extends ItemStack> representations = recipes.stream().map(representationFunction).collect(Collectors.toList());
		player.openInventory(new ListRecipesInventoryHolder(recipeType, representations).getInventory());
		
		return true;
	}
	
	private boolean listSender(CommandSender sender, String recipeType, List<? extends Recipe> recipes) {
		BiConsumer<? super Recipe, ? super CommandSender> recipeDisplayFunction = recipeToCommandSenderDiplayMap.get(recipeType);
		if (recipeDisplayFunction == null) {
			sender.sendMessage(ChatColor.RED + "No display function found for this type of recpie.");
			return true;
		}
		
		recipes.forEach(recipe -> recipeDisplayFunction.accept(recipe, sender));
		
		return true;
	}

}
