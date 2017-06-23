package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class ShapelessAdder implements BiConsumer<Player, List<String>> {

	private final CustomRecipesPlugin plugin;

	public ShapelessAdder(CustomRecipesPlugin plugin) {
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
			player.sendMessage(ChatColor.RED + "Usage: /addrecipe shapeless <key> [<group>]");
			return;
		}

		String keyString = args.get(0);
		String group = args.size() >= 2 ? args.get(1) : "";
		NamespacedKey bukkitKey = new NamespacedKey(plugin, keyString);

		ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);
		MinecraftKey key = CraftNamespacedKey.toMinecraft(bukkitKey);

		player.openInventory(new ShapelessRecipeHolder(plugin, result, key, group, player).getInventory());
	}

	private static final class ShapelessRecipeHolder implements InventoryHolder, Listener {

		private final Inventory dispenserInventory;
		private final CustomRecipesPlugin plugin;
		private final ItemStack result;
		private final String group;
		private final MinecraftKey key;
		private final Player callbackPlayer;

		public ShapelessRecipeHolder(CustomRecipesPlugin plugin, ItemStack result, MinecraftKey key, String group,
				Player callbackPlayer) {
			this.plugin = plugin;
			this.callbackPlayer = callbackPlayer;
			this.result = result;
			this.key = key;
			this.group = group;
			this.dispenserInventory = plugin.getServer().createInventory(this, InventoryType.DISPENSER, "Create a shapeless recipe!");
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}

		@Override
		public Inventory getInventory() {
			return dispenserInventory;
		}

		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof ShapelessRecipeHolder) {
				ShapelessRecipeHolder holder = (ShapelessRecipeHolder) event.getInventory().getHolder();
				if (holder != this) return;

				ShapelessRecipes nmsRecipe = holder.toRecipe();
				CRShapelessRecipe<ShapelessRecipes> shapelessRecipe = new CRShapelessRecipe<>(nmsRecipe);

				List<List<String>> recipeIngredients = shapelessRecipe
					.getIngredients().stream().map((CRChoiceIngredient ingr) -> ingr.getChoices().stream()
						.map(InventoryUtils::getItemName)
						.collect(Collectors.toList()))
					.collect(Collectors.toList());
				String recipeString = recipeIngredients + "" + ChatColor.RESET + " -> "
						+ InventoryUtils.getItemName(shapelessRecipe.getResult());

				holder.plugin.getCraftingManager().addRecipe(holder.key, nmsRecipe, shapelessRecipe);
				holder.callbackPlayer.sendMessage(String.format("%sAdded shapeless recipe: %s%s%s!",
						ChatColor.GREEN, ChatColor.WHITE, recipeString, ChatColor.WHITE));

				HandlerList.unregisterAll(holder);
			}
		}

		private ShapelessRecipes toRecipe() {
			CraftInventoryCustom dispenserInventory = (CraftInventoryCustom) this.dispenserInventory;
			IInventory minecraftInventory = (IInventory) ReflectionUtil.getDeclaredFieldValue(dispenserInventory, "inventory");
			NonNullList<ItemStack> itemStacks = (NonNullList<ItemStack>) ReflectionUtil.getDeclaredFieldValue(minecraftInventory, "items");
			
			NonNullList<RecipeItemStack> ingredients = itemStacks.stream()
					.filter(is -> !is.isEmpty())
					.map(is -> RecipeItemStack.a(new ItemStack[] {is}))
					.collect(Collectors.toCollection(NonNullList::a));
			
			ShapelessRecipes recipe = new ShapelessRecipes(group, result, ingredients);
			recipe.setKey(key);
			return recipe;
		}

	}

}
