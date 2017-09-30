package com.gmail.jannyboy11.customrecipes.commands;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.gui.CraftingRecipeMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManageRecipesCommandExecutor implements CommandExecutor {

    private final CustomRecipesPlugin plugin;

    public ManageRecipesCommandExecutor(CustomRecipesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        player.openInventory(new CraftingRecipeMenu(plugin, new ItemStack[0][]).getInventory());

        return true;
    }

}
