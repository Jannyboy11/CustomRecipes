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

public class RemoveRecipeCommandExecutor implements CommandExecutor {
	
	private final Map<String, ? extends BiConsumer<? super Player, ? super List<String>>> recipeRemovers;
	
	public RemoveRecipeCommandExecutor(Map<String, ? extends BiConsumer<? super Player, ? super List<String>>> recipeRemovers) {
		this.recipeRemovers = recipeRemovers;
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
		BiConsumer<? super Player, ? super List<String>> remover = recipeRemovers.get(type);
		if (remover == null) {
			sender.sendMessage(ChatColor.RED + "Unrecognized recipe type " + ChatColor.WHITE + args[0] + ChatColor.RED + ".");
			sender.sendMessage(ChatColor.RED + "Please choose from " + ChatColor.WHITE + recipeRemovers.keySet().toString() + ChatColor.RED + ".");
			return true;
		}
		
		try {
			remover.accept(player, Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			player.sendMessage(ChatColor.RED + "Something went wrong when trying to remove a recipe. See the console for errors.");
		}
		
		return true;
	}

}
