package com.gmail.jannyboy11.customrecipes.commands;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class ListRecipesCommandExecutor implements CommandExecutor {
	
	private final Function<String, ? extends List<? extends Recipe>> recipesByTypeMapper;
	private final Map<String, Function<? super Recipe, ? extends ItemStack>> recipeToItemMap;
	private final Map<String, BiConsumer<? super Recipe, ? extends CommandSender>> recipeToCommandSenderDiplayMap;
	
	public ListRecipesCommandExecutor(Function<String, ? extends List<? extends Recipe>> recipesByTypeMapper,
			Map<String, Function<? super Recipe, ? extends ItemStack>> recipeToItemMap,
			Map<String, BiConsumer<? super Recipe, ? extends CommandSender>> recipeToCommandSenderDiplayMap) {
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
		
		
		return true;
	}
	
	private boolean listSender(CommandSender sender, String recipeType, List<? extends Recipe> recipes) {
		
		
		return true;
	}

}
