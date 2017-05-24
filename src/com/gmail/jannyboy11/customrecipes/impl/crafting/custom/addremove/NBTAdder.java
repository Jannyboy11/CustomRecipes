package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapelessAdder.ShapelessRecipeHolder;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NBTAdder implements BiConsumer<Player, List<String>> {
	
	private final CustomRecipesPlugin plugin;
	
	public NBTAdder(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}
	

	@Override
	public void accept(Player player, List<String> args) {
		org.bukkit.inventory.ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		if (itemInMainHand == null) {
			player.sendMessage(ChatColor.RED + "Put the result of the recipe in your main hand when executing this command.");
			return;
		}

		if (args.isEmpty()) {
			player.sendMessage(ChatColor.RED + "Usage: /addrecipe NBT <key> [<group>]");
			return;
		}

		String keyString = args.get(0);
		String group = args.size() >= 2 ? args.get(1) : "";
		NamespacedKey bukkitKey = new NamespacedKey(plugin, keyString);

		ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);
		MinecraftKey key = CraftNamespacedKey.toMinecraft(bukkitKey);

		player.openInventory(new NBTRecipeHolder(plugin, result, key, group, player).getInventory());
	}

	
	private static final class NBTRecipeHolder implements InventoryHolder, Listener {
		
		private final Inventory dispenserInventory;
		private final CustomRecipesPlugin plugin;
		private final ItemStack result;
		private final MinecraftKey key;
		private final String group;
		private final Player callbackPlayer;
		
		public NBTRecipeHolder(CustomRecipesPlugin plugin, ItemStack result, MinecraftKey key, String group, Player callbackPlayer) {
			this.plugin = plugin;
			this.result = result;
			this.key = key;
			this.group = group;
			this.callbackPlayer = callbackPlayer;
			this.dispenserInventory = plugin.getServer().createInventory(this, InventoryType.DISPENSER, "Create an NBT recipe!");
		}

		@Override
		public Inventory getInventory() {
			return dispenserInventory;
		}
		
	}
	
}
