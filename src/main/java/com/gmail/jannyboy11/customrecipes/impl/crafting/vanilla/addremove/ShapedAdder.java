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
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;


public class ShapedAdder implements BiConsumer<Player, List<String>> {
	
	private final CustomRecipesPlugin plugin;
	
	public ShapedAdder (CustomRecipesPlugin plugin) {
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
			player.sendMessage(ChatColor.RED + "Usage: /addrecipe shaped <key> [<group>]");
			return;
		}
		
		String keyString = args.get(0);
		String group = args.size() >= 2 ? args.get(1) : "";
		NamespacedKey bukkitKey = plugin.getKey(keyString);
		
		ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);
		MinecraftKey key = CraftNamespacedKey.toMinecraft(bukkitKey); 
		
		player.openInventory(new ShapedRecipeHolder(player, plugin, result, key, group).getInventory());
	}

	private static final class ShapedRecipeHolder implements InventoryHolder, Listener {
		
		private final Inventory dispenserInventory;
		private final CustomRecipesPlugin plugin;
		private final ItemStack result;
		private final Player callbackPlayer;
		private final String group;
		private final MinecraftKey key;
		
		public ShapedRecipeHolder(Player callbackPlayer, CustomRecipesPlugin plugin, ItemStack result, MinecraftKey key, String group) {
			this.plugin = plugin;
			this.dispenserInventory = plugin.getServer().createInventory(this, InventoryType.DISPENSER, "Create a shaped recipe!");
			this.result = result;
			this.callbackPlayer = callbackPlayer;
			this.group = group;
			this.key = key;
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}
		
		@Override
		public Inventory getInventory() {
			return dispenserInventory;
		}
		
		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			if (event.getInventory().getHolder() instanceof ShapedRecipeHolder) {
				ShapedRecipeHolder holder = (ShapedRecipeHolder) event.getInventory().getHolder();
				if (holder != this) return;
				
				Inventory inventory = event.getInventory();
				if (InventoryUtils.isEmpty(inventory)) {
					holder.callbackPlayer.sendMessage(ChatColor.RED + "Do you seriously want to create a recipe without ingredients?");
					return;
				}
				
				ShapedRecipes nmsRecipe = holder.toRecipe();
				CRShapedRecipe<ShapedRecipes> shapedRecipe = new CRShapedRecipe<>(nmsRecipe);
				List<List<String>> recipeIngredients = shapedRecipe.getIngredients().stream()
					.map((CRChoiceIngredient ingr) -> ingr.getChoices().stream()
						.map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList());
				String recipeString = recipeIngredients + "" +
					ChatColor.RESET + " -> " +
					InventoryUtils.getItemName(shapedRecipe.getResult());
				
				holder.plugin.getCraftingManager().addRecipe(holder.key, nmsRecipe, shapedRecipe);
				holder.callbackPlayer.sendMessage(String.format("%sAdded shaped recipe: %s%s%s!",
						ChatColor.GREEN, ChatColor.WHITE, recipeString, ChatColor.WHITE));
				
				HandlerList.unregisterAll(holder);
			}
		}

		private ShapedRecipes toRecipe() {
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
			
			ShapedRecipes recipe = new ShapedRecipes(group, width, height, ingredients, result);
			recipe.setKey(key);
			return recipe;
		}
		
	}
	
}
