package com.gmail.jannyboy11.customrecipes.commands;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddRecipeCommandExecutor implements CommandExecutor {
	
	private final Map<String, ? extends BiConsumer<? super Player, ? super List<String>>> recipeAdders;
	
	public AddRecipeCommandExecutor(Map<String, ? extends BiConsumer<? super Player, ? super List<String>>> recipeAdders) {
		this.recipeAdders = recipeAdders;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You can only use this command as a player.");
			return true;
		}
		
		if (args.length == 0) return false;
		Player player = (Player) sender;
		
		String type = args[0];		
		BiConsumer<? super Player, ? super List<String>> adder = recipeAdders.get(type);
		if (adder == null) {
			sender.sendMessage(ChatColor.RED + "Unrecognized recipe type " + ChatColor.WHITE + type + ChatColor.RED + ".");
			sender.sendMessage(ChatColor.RED + "Please choose from " + ChatColor.WHITE + recipeAdders.keySet().toString() + ChatColor.RED + ".");
			return true;
		}
		
		try {
			adder.accept(player, Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			player.sendMessage(ChatColor.RED + "Something went wrong when trying to create a new recipe. Check the console for errors.");
		}
		
		return true;
	}

}
