package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
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
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.WorldRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRWorldRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class WorldAdder implements BiConsumer<Player, List<String>> {

	private final CustomRecipesPlugin plugin;

	public WorldAdder(CustomRecipesPlugin plugin) {
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
			player.sendMessage(ChatColor.RED + "Usage: /addrecipe world <key> [<world name>] [<group>]");
			return;
		}

		UUID world = player.getWorld().getUID();
		if (args.size() >= 2) {
			World bukkitWorld = plugin.getServer().getWorld(args.get(1));
			if (bukkitWorld != null) world = bukkitWorld.getUID();
		}
		
		String keyString = args.get(0);	
		String group = args.size() >= 3 ? args.get(2) : "";
		NamespacedKey bukkitKey = plugin.getKey(keyString);

		ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);
		MinecraftKey key = CraftNamespacedKey.toMinecraft(bukkitKey);

		player.openInventory(new WorldRecipeHolder(plugin, result, key, group, player, world).getInventory());
	}


	private static final class WorldRecipeHolder implements InventoryHolder, Listener {

		private final Inventory dispenserInventory;
		private final CustomRecipesPlugin plugin;
		private final ItemStack result;
		private final MinecraftKey key;
		private final String group;
		private final UUID world;
		private final Player callbackPlayer;

		public WorldRecipeHolder(CustomRecipesPlugin plugin, ItemStack result, MinecraftKey key, String group, Player callbackPlayer, UUID world) {
			this.plugin = plugin;
			this.result = result;
			this.key = key;
			this.group = group;
			this.world = world;
			this.callbackPlayer = callbackPlayer;
			this.dispenserInventory = plugin.getServer().createInventory(this, InventoryType.DISPENSER, "Create a world recipe!");
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}

		@Override
		public Inventory getInventory() {
			return dispenserInventory;
		}


		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof WorldRecipeHolder) {
				WorldRecipeHolder holder = (WorldRecipeHolder) event.getInventory().getHolder();
				if (holder != this) return;

				Inventory inventory = event.getInventory();
				if (InventoryUtils.isEmpty(inventory)) {
					holder.callbackPlayer.sendMessage(ChatColor.RED + "Do you seriously want to create a recipe without ingredients?");
					return;
				}

				WorldRecipe nmsRecipe = holder.toRecipe();
				CRWorldRecipe worldRecipe = new CRWorldRecipe(nmsRecipe);
				List<List<String>> recipeIngredients = worldRecipe.getIngredients().stream()
						.map((CRChoiceIngredient ingr) -> ingr.getChoices().stream()
								.map(InventoryUtils::getItemName).collect(Collectors.toList()))
						.collect(Collectors.toList());
				String recipeString = recipeIngredients + "" +
						ChatColor.RESET + " -> " +
						InventoryUtils.getItemName(worldRecipe.getResult());

				boolean success = holder.plugin.getCraftingManager().addRecipe(holder.key, nmsRecipe, worldRecipe);
				if (success) {
					holder.callbackPlayer.sendMessage(String.format("%sAdded world recipe: %s%s%s!",
							ChatColor.GREEN, ChatColor.WHITE, recipeString, ChatColor.WHITE));
					plugin.saveCraftingRecipeFile("count", worldRecipe);
				} else {
					holder.callbackPlayer.sendMessage(ChatColor.RED + "Couldn't create a world recipe. Possibly a duplicate key.");
				}

				HandlerList.unregisterAll(holder);
			}
		}


		private WorldRecipe toRecipe() {
			CraftInventoryCustom dispenserInventory = (CraftInventoryCustom) this.dispenserInventory;
			IInventory minecraftInventory = (IInventory) ReflectionUtil.getDeclaredFieldValue(dispenserInventory, "inventory");
			NonNullList<ItemStack> dispenserInventoryContents = (NonNullList<ItemStack>) ReflectionUtil.getDeclaredFieldValue(minecraftInventory, "items");

			int minNonEmptyRownum = 2;
			int minNonEmptyColnum = 2;
			int maxNonEmptyRownum = 0;
			int maxNonEmptyColnum = 0;

			for (int index = 0; index < dispenserInventoryContents.size(); index++) {
				ItemStack stack = dispenserInventoryContents.get(index);
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

					ItemStack ingredientStack = dispenserInventoryContents.get(InventoryUtils.inventoryIndex(3,
							new int[] {h + minNonEmptyRownum, w + minNonEmptyColnum}));

					RecipeItemStack vanillaIngredient = RecipeItemStack.a(new ItemStack[] {ingredientStack});

					ingredients.set(index, vanillaIngredient);
				}
			}

			WorldRecipe worldRecipe = new WorldRecipe(group, width, height, ingredients, result, world);
			worldRecipe.setKey(key);
			return worldRecipe;
		}

	}

}
