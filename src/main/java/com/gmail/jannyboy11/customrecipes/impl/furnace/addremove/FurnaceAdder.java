package com.gmail.jannyboy11.customrecipes.impl.furnace.addremove;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
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
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.RecipesFurnace;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class FurnaceAdder implements BiConsumer<Player, List<String>> {

	private final CustomRecipesPlugin plugin;

	public FurnaceAdder(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void accept(Player player, List<String> args) {
		org.bukkit.inventory.ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		if (InventoryUtils.isEmptyStack(itemInMainHand)) {
			player.sendMessage(ChatColor.RED + "Put the result of the recipe in your main hand when executing this command.");
			return;
		}

		boolean hasXp = !args.isEmpty();
		float xp = 0;
		if (hasXp) {
			try {
				xp = Float.parseFloat(args.get(0));
			} catch (NumberFormatException ignored) {
				hasXp = false;
			}
		}
		boolean vanilla = args.size() > 1 ? "vanilla".equalsIgnoreCase(args.get(1)) : false;

		ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);
		player.openInventory(new FurnaceRecipeHolder(plugin, player, result, vanilla, hasXp, xp).getInventory());
	}

	public static class FurnaceRecipeHolder implements InventoryHolder, Listener {

		private final CustomRecipesPlugin plugin;
		private final Inventory hopperInventory;
		private final Player callbackPlayer;
		private final ItemStack result;
		private final boolean vanilla;
		private final boolean hasXp;
		private final float xp;

		public FurnaceRecipeHolder(CustomRecipesPlugin plugin, Player player, ItemStack result, boolean vanilla, boolean hasXp, float xp) {
			this.plugin = plugin;
			this.callbackPlayer = player;
			this.hopperInventory = plugin.getServer().createInventory(this, InventoryType.HOPPER, "Create a furnace recipe!");
			this.result = result;
			this.vanilla = vanilla;
			this.hasXp = hasXp;
			this.xp = hasXp ? xp : 0F;
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}

		@Override
		public Inventory getInventory() {
			return hopperInventory;
		}

		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof FurnaceRecipeHolder) {
				FurnaceRecipeHolder holder = (FurnaceRecipeHolder) event.getInventory().getHolder();
				if (holder != this) return;

				Inventory inventory = event.getInventory();
				if (InventoryUtils.isEmptyStack(inventory.getItem(2))) {
					holder.callbackPlayer.sendMessage(ChatColor.RED + "Please put your ingredient int the middle slot.");

					HandlerList.unregisterAll(holder);
					return;
				}

				CRFurnaceRecipe furnaceRecipe = new CRFurnaceRecipe(registerRecipe());
				String ingredientString = InventoryUtils.getItemName(furnaceRecipe.getIngredient());

				String recipeString = ingredientString + "" + ChatColor.RESET + " -> "
						+ InventoryUtils.getItemName(furnaceRecipe.getResult());
				if (holder.hasXp) {
					DecimalFormat decimalFormat = new DecimalFormat("##.##");
					String xpString = decimalFormat.format(holder.xp);
					recipeString += ChatColor.RESET + " (" + xpString + " xp)";
				}

				holder.callbackPlayer.sendMessage(String.format("%sAdded furnace recipe: %s%s%s!",
						ChatColor.GREEN, ChatColor.WHITE, recipeString, ChatColor.WHITE));

				HandlerList.unregisterAll(holder);
			}
		}

		private FurnaceRecipe registerRecipe() {
			CraftInventoryCustom dispenserInventory = (CraftInventoryCustom) this.hopperInventory;
			IInventory minecraftInventory = (IInventory) ReflectionUtil.getDeclaredFieldValue(dispenserInventory, "inventory");
			NonNullList<ItemStack> itemStacks = (NonNullList<ItemStack>) ReflectionUtil.getDeclaredFieldValue(minecraftInventory, "items");

			ItemStack ingredient = itemStacks.get(2);

			RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();

			Map<ItemStack, ItemStack> recipes = vanilla ? recipesFurnace.recipes : recipesFurnace.customRecipes;
			Map<ItemStack, Float> xps = vanilla ? CRFurnaceManager.vanillaXp(recipesFurnace) : recipesFurnace.customExperience;

			FurnaceRecipe furnaceRecipe = new FurnaceRecipe(recipes, xps, ingredient);
			furnaceRecipe.setResult(result);
			if (hasXp) furnaceRecipe.setXp(xp);

			return furnaceRecipe;
		}

	}

}
