package com.gmail.jannyboy11.customrecipes;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapedRecipe;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftShapelessRecipe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.jannyboy11.customrecipes.api.CustomRecipesApi;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.SimpleFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.commands.AddRecipeCommandExecutor;
import com.gmail.jannyboy11.customrecipes.commands.ListRecipesCommandExecutor;
import com.gmail.jannyboy11.customrecipes.commands.RemoveRecipeCommandExecutor;
import com.gmail.jannyboy11.customrecipes.gui.ListRecipesListener;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove.NBTAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapedAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapelessAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRVanillaRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CustomRecipesPlugin extends JavaPlugin implements CustomRecipesApi {
	
	private final NavigableMap<String, BiConsumer<? super Player, ? super List<String>>> adders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final NavigableMap<String, BiConsumer<? super Player, ? super List<String>>> removers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	private final Map<String, Supplier<? extends List<? extends Recipe>>> recipeSuppliers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, Function<? super Recipe, ? extends ItemStack>> recipeToItemMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, BiConsumer<? super Recipe, ? super CommandSender>> recipeToCommandSenderDiplayMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	private CRCraftingManager craftingManager = new CRCraftingManager();
	private CRFurnaceManager furnaceManager = new CRFurnaceManager();
	
	@Override
	public void onLoad() {
		//define RecipeItemstackInjected subclass
		InjectedIngredient.inject();
		
		//adders and removers
		addAdder("shaped", new ShapedAdder(this));
		addAdder("shapeless", new ShapelessAdder(this));
		addAdder("nbt", new NBTAdder(this));
		//TODO permission
		//TODO furnace
		
		//TODO add standard removers
		
		
		//representations for the listrecipes menu
		recipeToItemMap.put("shaped", recipe -> ((ShapedRecipe) recipe).getRepresentation());
		recipeToItemMap.put("shapeless", recipe -> ((ShapelessRecipe) recipe).getRepresentation());
		recipeToItemMap.put("furnace", recipe -> ((FurnaceRecipe) recipe).getRepresentation());
		
		//recipe displayers
		recipeToCommandSenderDiplayMap.put("shaped", (recipe, commandSender) -> {
			ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
			commandSender.sendMessage("---Shaped Recipe---");
			commandSender.sendMessage("Key: " + shapedRecipe.getKey());
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(shapedRecipe.getResult()));
			commandSender.sendMessage("Width: " + shapedRecipe.getWidth());
			commandSender.sendMessage("Height: " + shapedRecipe.getHeight());
			commandSender.sendMessage("Ingredients: " + shapedRecipe.getIngredients().stream()
					.map(ingr -> ingr.getChoices().stream().map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList()));
			if (shapedRecipe.hasGroup()) commandSender.sendMessage("Group: " + shapedRecipe.getGroup());
			if (shapedRecipe.isHidden()) commandSender.sendMessage("Hidden: true");
			commandSender.sendMessage("---Shaped Recipe---");
		});
		
		recipeToCommandSenderDiplayMap.put("shapeless", (recipe, commandSender) -> {
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
			commandSender.sendMessage("---Shapeless Recipe---");
			commandSender.sendMessage("Key: " + shapelessRecipe.getKey());
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(shapelessRecipe.getResult()));
			commandSender.sendMessage("Ingredients: " + shapelessRecipe.getIngredients().stream()
					.map(ingr -> ingr.getChoices().stream().map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList()));
			if (shapelessRecipe.hasGroup()) commandSender.sendMessage("Group: " + shapelessRecipe.getGroup());
			if (shapelessRecipe.isHidden()) commandSender.sendMessage("Hidden: true");
			commandSender.sendMessage("---Shapeless Recipe---");
		});
		
		recipeToCommandSenderDiplayMap.put("furnace", (recipe, commandSender) -> {
			FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
			commandSender.sendMessage("---Shapeless Recipe---");
			commandSender.sendMessage("Ingredient: " + InventoryUtils.getItemName(furnaceRecipe.getIngredient()));
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(furnaceRecipe.getResult()));
			if (furnaceRecipe.hasXp()) commandSender.sendMessage("XP: " + furnaceRecipe.getXp());
			commandSender.sendMessage("---Shapeless Recipe---");
		});
		
		//recipe providers
		recipeSuppliers.put("shaped", () -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(craftingManager.iterator(),
					Spliterator.NONNULL), false)
					.filter(recipe -> recipe instanceof ShapedRecipe)
					.collect(Collectors.toList()));
		recipeSuppliers.put("shapeless", () -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(craftingManager.iterator(),
				Spliterator.NONNULL), false)
				.filter(recipe -> recipe instanceof ShapelessRecipe)
				.collect(Collectors.toList()));
		recipeSuppliers.put("furnace", () -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(furnaceManager.iterator(),
				Spliterator.NONNULL), false)
				.collect(Collectors.toList()));
	}
	
	
	public boolean addAdder(String recipeType, BiConsumer<? super Player, ? super List<String>> adder) {
		return adders.putIfAbsent(recipeType, adder) == null;
	}
	
	
	
	@Override
	public void onEnable() {
		getCommand("addrecipe").setExecutor(new AddRecipeCommandExecutor(Collections.unmodifiableNavigableMap(adders)));
		getCommand("removerecipe").setExecutor(new RemoveRecipeCommandExecutor(Collections.unmodifiableNavigableMap(removers)));
		getCommand("listrecipes").setExecutor(new ListRecipesCommandExecutor(this::getRecipes,
				Collections.unmodifiableMap(recipeToItemMap),
				Collections.unmodifiableMap(recipeToCommandSenderDiplayMap)));
		
		getServer().getPluginManager().registerEvents(new ListRecipesListener(), this);
	}
	
	
	
	
	
	public static CustomRecipesPlugin getInstance() {
		return JavaPlugin.getPlugin(CustomRecipesPlugin.class);
	}
	
	
	
	
	@Override
	public CRCraftingManager getCraftingManager() {
		return craftingManager;
	}
	
	@Override
	public CRFurnaceManager getFurnaceManager() {
		return furnaceManager;
	}

	
	
	@Override
	public boolean isVanillaRecipeType(CraftingRecipe recipe) {
		if (!(recipe instanceof CRVanillaRecipe)) return false;
		
		CRVanillaRecipe<? extends IRecipe> vanillaWrapper = (CRVanillaRecipe<? extends IRecipe>) recipe;
		return vanillaWrapper.getHandle().getClass().getName().startsWith("net.minecraft.server.");
	}
	
	@Override
	public ShapedRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapedRecipe bukkitRecipe) {
		CraftShapedRecipe craftShapedRecipe = CraftShapedRecipe.fromBukkitRecipe(bukkitRecipe);
		//TODO this is still null
		ShapedRecipes nmsRecipe = (ShapedRecipes) ReflectionUtil.getDeclaredFieldValue(craftShapedRecipe, "recipe");
		return new CRShapedRecipe<>(nmsRecipe);
	}
	
	@Override
	public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe) {
		CraftShapelessRecipe craftShapelessRecipe = CraftShapelessRecipe.fromBukkitRecipe(bukkitRecipe);
		//TODO this is still null
		ShapelessRecipes nmsRecipe = (ShapelessRecipes) ReflectionUtil.getDeclaredFieldValue(craftShapelessRecipe, "recipe");
		return new CRShapelessRecipe<>(nmsRecipe);
	}
	
	@Override
	public FurnaceRecipe asCustomRecipesMirror(org.bukkit.inventory.FurnaceRecipe bukkitRecipe) {
		SimpleFurnaceRecipe simple = new SimpleFurnaceRecipe(bukkitRecipe.getInput(), bukkitRecipe.getResult(), bukkitRecipe.getExperience());
		
		CRFurnaceRecipe recipe = furnaceManager.getRecipe(bukkitRecipe.getInput());
		return simple.equals(recipe) ? recipe : simple;
	}
	
	
	
	//for NMS developers
	
	public void setCraftingManager(CRCraftingManager craftingManager) {
		this.craftingManager = Objects.requireNonNull(craftingManager);
	}
	
	public void setFurnaceManager(CRFurnaceManager furnaceManager) {
		this.furnaceManager = Objects.requireNonNull(furnaceManager);
	}

	
	public List<? extends Recipe> getRecipes(String type) {
		return recipeSuppliers.getOrDefault(type, Collections::emptyList).get();
	}

}
