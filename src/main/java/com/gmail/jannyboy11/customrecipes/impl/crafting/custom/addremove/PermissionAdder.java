package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove;

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
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.PermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRPermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class PermissionAdder implements BiConsumer<Player, List<String>> {

	private final CustomRecipesPlugin plugin;

	public PermissionAdder(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void accept(Player player, List<String> args) {
		org.bukkit.inventory.ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
		if (itemInMainHand == null) {
			player.sendMessage(ChatColor.RED + "Put the result of the recipe in your main hand when executing this command.");
			return;
		}

		if (args.size() < 2) {
			player.sendMessage(ChatColor.RED + "Usage: /addrecipe permission <key> <permission> [<group>]");
			return;
		}

		String keyString = args.get(0);
		String permission = args.get(1);
		String group = args.size() >= 3 ? args.get(2) : "";
		NamespacedKey bukkitKey = plugin.getKey(keyString);

		ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);
		MinecraftKey key = CraftNamespacedKey.toMinecraft(bukkitKey);

		player.openInventory(new PermissionRecipeHolder(plugin, result, key, group, player, permission).getInventory());
	}


	public static class PermissionRecipeHolder implements InventoryHolder, Listener {

		private final CustomRecipesPlugin plugin;
		private final ItemStack result;
		private final MinecraftKey key;
		private final String group;
		private final Player callbackPlayer;
		private final String permission;
		private final Inventory dispenserInventory;

		public PermissionRecipeHolder(CustomRecipesPlugin plugin, ItemStack result,
				MinecraftKey key, String group, Player player, String permission) {
			this.plugin = plugin;
			this.result = result;
			this.key = key;
			this.group = group;
			this.callbackPlayer = player;
			this.permission = permission;
			this.dispenserInventory = plugin.getServer().createInventory(this, InventoryType.DISPENSER, "Create a permission recipe!");
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}

		@Override
		public Inventory getInventory() {
			return dispenserInventory;
		}

		@EventHandler
		public void onInventoryClick(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof PermissionRecipeHolder) {
				PermissionRecipeHolder holder = (PermissionRecipeHolder) event.getView().getTopInventory().getHolder();
				if (holder != this) return;

				Inventory inventory = event.getInventory();
				if (InventoryUtils.isEmpty(inventory)) {
					holder.callbackPlayer.sendMessage(ChatColor.RED + "Do you seriously want to create a recipe without ingredients?");
					return;
				}

				PermissionRecipe nmsRecipe = holder.toRecipe();
				CRPermissionRecipe permissionRecipe = new CRPermissionRecipe(nmsRecipe);

				List<List<String>> recipeIngredients = permissionRecipe.getIngredients().stream()
						.map((CRChoiceIngredient ingr) -> ingr.getChoices().stream()
								.map(InventoryUtils::getItemName).collect(Collectors.toList()))
						.collect(Collectors.toList());
				String recipeString = recipeIngredients + "" +
						ChatColor.RESET + " -> " +
						InventoryUtils.getItemName(permissionRecipe.getResult());

				boolean success = holder.plugin.getCraftingManager().addRecipe(holder.key, nmsRecipe, permissionRecipe);
				if (success) {
					holder.callbackPlayer.sendMessage(String.format("%sAdded a permission recipe: %s%s%s!",
							ChatColor.GREEN, ChatColor.WHITE, recipeString, ChatColor.WHITE));
					plugin.saveCraftingRecipeFile("permission", permissionRecipe);
				} else {
					holder.callbackPlayer.sendMessage(ChatColor.RED + "Couldn't create a permission recipe. Possibly a duplicate key.");
				}

				HandlerList.unregisterAll(holder);
			}
		}

		private PermissionRecipe toRecipe() {
			CraftInventoryCustom dispenserInventory = (CraftInventoryCustom) this.dispenserInventory;
			IInventory minecraftInventory = (IInventory) ReflectionUtil.getDeclaredFieldValue(dispenserInventory, "inventory");
			NonNullList<ItemStack> itemStacks = (NonNullList<ItemStack>) ReflectionUtil.getDeclaredFieldValue(minecraftInventory, "items");

			int minNonEmptyRownum = 2;
			int minNonEmptyColnum = 2;
			int maxNonEmptyRownum = 0;
			int maxNonEmptyColnum = 0;

			for (int index = 0; index < itemStacks.size(); index++) {
				ItemStack stack = itemStacks.get(index);
				if (!stack.isEmpty()) {
					int[] rownumColnum = InventoryUtils.inventoryRownumColnum(3, index);
					int rowNum = rownumColnum[0];
					int colNum = rownumColnum[1];

					minNonEmptyRownum = Math.min(minNonEmptyRownum, rowNum);
					minNonEmptyColnum = Math.min(minNonEmptyColnum, colNum);
					maxNonEmptyRownum = Math.max(maxNonEmptyRownum, rowNum);
					maxNonEmptyColnum = Math.max(maxNonEmptyColnum, colNum);
				}
			}

			int height = maxNonEmptyRownum - minNonEmptyRownum + 1;
			int width = maxNonEmptyColnum - minNonEmptyColnum + 1;

			NonNullList<RecipeItemStack> ingredients = NonNullList.a(height * width, RecipeItemStack.a);
			for (int h = 0; h < height; h++) {
				for (int w = 0; w < width; w++) {
					int index = InventoryUtils.inventoryIndex(width, new int[] {h, w});

					ItemStack ingredientStack = itemStacks.get(InventoryUtils.inventoryIndex(3,
							new int[] {h + minNonEmptyRownum, w + minNonEmptyColnum}));
					RecipeItemStack ingredient = RecipeItemStack.a(new ItemStack[] {ingredientStack});

					ingredients.set(index, ingredient);
				}
			}

			PermissionRecipe recipe = new PermissionRecipe(group, width, height, ingredients, result, permission);
			recipe.setKey(key);
			return recipe;
		}

	}

}
