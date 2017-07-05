package com.gmail.jannyboy11.customrecipes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.jannyboy11.customrecipes.api.CustomRecipesApi;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.SimpleChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.SimpleShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.SimpleShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient.SimilarIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.custom.ingredient.WildcardIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe.NBTRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe.PermissionRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.BannerDuplicateRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.SimpleFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.commands.AddRecipeCommandExecutor;
import com.gmail.jannyboy11.customrecipes.commands.ListRecipesCommandExecutor;
import com.gmail.jannyboy11.customrecipes.commands.MigrateRecipesCommandExecutor;
import com.gmail.jannyboy11.customrecipes.commands.RemoveRecipeCommandExecutor;
import com.gmail.jannyboy11.customrecipes.gui.ListRecipesListener;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove.NBTAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove.NBTRemover;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove.PermissionAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.addremove.PermissionRemover;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.Bukkit2NMSIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRNBTRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRPermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapedAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapedRemover;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapelessAdder;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.addremove.ShapelessRemover;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CREmptyIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRArmorDyeRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBannerAddPatternRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRBookCloneRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRFireworksRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapCloneRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRMapExtendRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRRepairRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShieldDecorationRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShulkerBoxDyeRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRTippedArrowRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRVanillaRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.addremove.FurnaceAdder;
import com.gmail.jannyboy11.customrecipes.impl.furnace.addremove.FurnaceRemover;
import com.gmail.jannyboy11.customrecipes.serialize.ConfigurationSerializableByteArray;
import com.gmail.jannyboy11.customrecipes.serialize.ConfigurationSerializableIntArray;
import com.gmail.jannyboy11.customrecipes.serialize.ConfigurationSerializableLongArray;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.CraftingManager;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.RecipesFurnace;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CustomRecipesPlugin extends JavaPlugin implements CustomRecipesApi {

	private final NavigableMap<String, BiConsumer<? super Player, ? super List<String>>> adders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final NavigableMap<String, BiConsumer<? super Player, ? super List<String>>> removers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private final Map<String, Supplier<? extends List<? extends Recipe>>> recipeSuppliers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, Function<? super Recipe, ? extends ItemStack>> recipeToItemMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, BiConsumer<? super Recipe, ? super CommandSender>> recipeToCommandSenderDiplayMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private final Map<String, BiConsumer<? super Recipe, ? super File>> writers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private final Map<String, Function<? super File, ? extends Recipe>> readers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	private CRCraftingManager craftingManager = new CRCraftingManager();
	private CRFurnaceManager furnaceManager = new CRFurnaceManager();

	private Set<MinecraftKey> vanillaCraftingRecipes;
	private Map<net.minecraft.server.v1_12_R1.ItemStack, net.minecraft.server.v1_12_R1.ItemStack> vanillaFurnaceRecipes;


	private void recordVanillaRecipes() {
		getServer().resetRecipes(); //Is called before onEnable of other plugins, so shouldn't cause trouble.
		vanillaCraftingRecipes = new HashSet<>(CraftingManager.recipes.keySet());
		vanillaFurnaceRecipes = new HashMap<>(RecipesFurnace.getInstance().recipes);
	}

	public boolean isVanillaCraftingRecipe(MinecraftKey key) {
		if (key == null) return false;
		return vanillaCraftingRecipes.contains(key);
	}

	public boolean isVanillaFurnaceRecipe(net.minecraft.server.v1_12_R1.ItemStack ingredient, net.minecraft.server.v1_12_R1.ItemStack result) {
		if (ingredient == null) return false;
		return vanillaFurnaceRecipes.entrySet().stream().anyMatch(entry -> 
			CRFurnaceManager.furnaceEquals(RecipesFurnace.getInstance(), entry.getKey(), ingredient) &&
			CRFurnaceManager.furnaceEquals(RecipesFurnace.getInstance(), entry.getValue(), result));
	}


	@Override
	public void onLoad() {
		//define RecipeItemStackInjected subclass
		InjectedIngredient.inject();		

		//let's hope no other plugins have added crafting recipes here
		recordVanillaRecipes();

		//adders
		addAdder("shaped", new ShapedAdder(this));
		addAdder("shapeless", new ShapelessAdder(this));
		addAdder("nbt", new NBTAdder(this));
		addAdder("permission", new PermissionAdder(this));
		addAdder("furnace", new FurnaceAdder(this));

		//removers
		addRemover("shaped", new ShapedRemover(this));
		addRemover("shapeless", new ShapelessRemover(this));
		addRemover("nbt", new NBTRemover(this));
		addRemover("permission", new PermissionRemover(this));
		addRemover("furnace", new FurnaceRemover(this));

		//readers and writers only used in commands, thats why casting to CR variants is allowed
		BiConsumer<Recipe, File> nbtSaver = (recipe, file) -> {
			NBTSerializable cr = (NBTSerializable) recipe;
			try {
				NBTUtil.writeNBTTagCompound(file, cr.serializeToNbt());
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		addWriter("shaped", nbtSaver);
		addWriter("shapeless", nbtSaver);
		addWriter("nbt", nbtSaver);
		addWriter("permission", nbtSaver);
		addWriter("furnace", nbtSaver);
		
		Function<File, NBTTagCompound> nbtReader = file -> {
			try {
				return NBTUtil.readNBTTagCompound(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		};
		addReader("shaped", nbtReader.andThen(CRShapedRecipe::new));
		addReader("shapeless", nbtReader.andThen(CRShapelessRecipe::new));
		addReader("nbt", nbtReader.andThen(CRNBTRecipe::new));
		addReader("permission", nbtReader.andThen(CRPermissionRecipe::new));
		addReader("furnace", nbtReader.andThen(CRFurnaceRecipe::new));
		

		//representations for the listrecipes menu
		recipeToItemMap.put("shaped", recipe -> ((ShapedRecipe) recipe).getRepresentation());
		recipeToItemMap.put("shapeless", recipe -> ((ShapelessRecipe) recipe).getRepresentation());
		recipeToItemMap.put("furnace", recipe -> ((FurnaceRecipe) recipe).getRepresentation());
		recipeToItemMap.put("nbt", recipe -> ((NBTRecipe) recipe).getRepresentation());
		recipeToItemMap.put("permission", recipe -> ((PermissionRecipe) recipe).getRepresentation());

		//recipe displayers
		recipeToCommandSenderDiplayMap.put("shaped", (recipe, commandSender) -> {
			ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
			commandSender.sendMessage("Key: " + craftingManager.getKey(shapedRecipe));
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(shapedRecipe.getResult()));
			commandSender.sendMessage("Width: " + shapedRecipe.getWidth());
			commandSender.sendMessage("Height: " + shapedRecipe.getHeight());
			commandSender.sendMessage("Ingredients: " + shapedRecipe.getIngredients().stream()
					.map(ingr -> ingr.getChoices().stream().map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList()));
			if (shapedRecipe.hasGroup()) commandSender.sendMessage("Group: " + shapedRecipe.getGroup());
			if (shapedRecipe.isHidden()) commandSender.sendMessage("Hidden: true");
			commandSender.sendMessage("");
		});
		recipeToCommandSenderDiplayMap.put("shapeless", (recipe, commandSender) -> {
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
			commandSender.sendMessage("Key: " + craftingManager.getKey(shapelessRecipe));
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(shapelessRecipe.getResult()));
			commandSender.sendMessage("Ingredients: " + shapelessRecipe.getIngredients().stream()
					.map(ingr -> ingr.getChoices().stream().map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList()));
			if (shapelessRecipe.hasGroup()) commandSender.sendMessage("Group: " + shapelessRecipe.getGroup());
			if (shapelessRecipe.isHidden()) commandSender.sendMessage("Hidden: true");
			commandSender.sendMessage("");
		});
		recipeToCommandSenderDiplayMap.put("furnace", (recipe, commandSender) -> {
			FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
			commandSender.sendMessage("Ingredient: " + InventoryUtils.getItemName(furnaceRecipe.getIngredient()));
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(furnaceRecipe.getResult()));
			if (furnaceRecipe.hasXp()) commandSender.sendMessage("XP: " + furnaceRecipe.getXp());
			commandSender.sendMessage("");
		});
		recipeToCommandSenderDiplayMap.put("nbt", (recipe, commandSender) -> {
			NBTRecipe nbtRecipe = (NBTRecipe) recipe;
			commandSender.sendMessage("Key: " + craftingManager.getKey(nbtRecipe));
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(nbtRecipe.getResult()));
			commandSender.sendMessage("Width: " + nbtRecipe.getWidth());
			commandSender.sendMessage("Height: " + nbtRecipe.getHeight());
			commandSender.sendMessage("Ingredients: " + nbtRecipe.getIngredients().stream()
					.map(ingr -> ingr.getChoices().stream().map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList()));
			if (nbtRecipe.hasGroup()) commandSender.sendMessage("Group: " + nbtRecipe.getGroup());
			if (nbtRecipe.isHidden()) commandSender.sendMessage("Hidden: true");
			commandSender.sendMessage("NBT specific");
			commandSender.sendMessage("");
		});
		recipeToCommandSenderDiplayMap.put("permission", (recipe, commandSender) -> {
			PermissionRecipe permissionRecipe = (PermissionRecipe) recipe;
			commandSender.sendMessage("Key: " + craftingManager.getKey(permissionRecipe));
			commandSender.sendMessage("Result: " + InventoryUtils.getItemName(permissionRecipe.getResult()));
			commandSender.sendMessage("Width: " + permissionRecipe.getWidth());
			commandSender.sendMessage("Height: " + permissionRecipe.getHeight());
			commandSender.sendMessage("Ingredients: " + permissionRecipe.getIngredients().stream()
					.map(ingr -> ingr.getChoices().stream().map(InventoryUtils::getItemName).collect(Collectors.toList()))
					.collect(Collectors.toList()));
			if (permissionRecipe.hasGroup()) commandSender.sendMessage("Group: " + permissionRecipe.getGroup());
			if (permissionRecipe.isHidden()) commandSender.sendMessage("Hidden: true");
			commandSender.sendMessage("Permission: " + permissionRecipe.getPermission());
			commandSender.sendMessage("");
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
		recipeSuppliers.put("nbt", () -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(craftingManager.iterator(),
				Spliterator.NONNULL), false)
				.filter(recipe -> recipe instanceof NBTRecipe)
				.collect(Collectors.toList()));
		recipeSuppliers.put("permission", () -> StreamSupport.stream(Spliterators.spliteratorUnknownSize(craftingManager.iterator(),
				Spliterator.NONNULL), false)
				.filter(recipe -> recipe instanceof PermissionRecipe)
				.collect(Collectors.toList()));
	}


	public boolean addAdder(String recipeType, BiConsumer<? super Player, ? super List<String>> adder) {
		return adders.putIfAbsent(recipeType, adder) == null;
	}

	public boolean addRemover(String recipeType, BiConsumer<? super Player, ? super List<String>> remover) {
		return removers.putIfAbsent(recipeType, remover) == null;
	}

	public boolean addWriter(String recipeType, BiConsumer<? super Recipe, ? super File> saver) {
		return writers.putIfAbsent(recipeType, saver) == null;
	}

	public boolean addReader(String recipeType, Function<? super File, ? extends Recipe> loader) {
		return readers.putIfAbsent(recipeType, loader) == null;
	}



	@Override
	public void onEnable() {
		//serializable stuff

		//nbt arrays
		ConfigurationSerialization.registerClass(ConfigurationSerializableByteArray.class);
		ConfigurationSerialization.registerClass(ConfigurationSerializableIntArray.class);
		ConfigurationSerialization.registerClass(ConfigurationSerializableLongArray.class);

		//bukkit ingredients
		ConfigurationSerialization.registerClass(SimilarIngredient.class);
		ConfigurationSerialization.registerClass(WildcardIngredient.class);
		ConfigurationSerialization.registerClass(SimpleChoiceIngredient.class);

		//bukkit recipes
		ConfigurationSerialization.registerClass(SimpleShapedRecipe.class);
		ConfigurationSerialization.registerClass(SimpleShapelessRecipe.class);

		//nms mirrors
		ConfigurationSerialization.registerClass(Bukkit2NMSIngredient.class);
		ConfigurationSerialization.registerClass(Bukkit2NMSRecipe.class);

		//nms ingredient wrappers
		ConfigurationSerialization.registerClass(CRChoiceIngredient.class);
		ConfigurationSerialization.registerClass(CREmptyIngredient.class);

		//nms recipe wrappers
		ConfigurationSerialization.registerClass(CRShapedRecipe.class);
		ConfigurationSerialization.registerClass(CRShapelessRecipe.class);
		ConfigurationSerialization.registerClass(CRNBTRecipe.class);
		ConfigurationSerialization.registerClass(CRPermissionRecipe.class);
		ConfigurationSerialization.registerClass(CRArmorDyeRecipe.class);
		ConfigurationSerialization.registerClass(CRBannerAddPatternRecipe.class);
		ConfigurationSerialization.registerClass(BannerDuplicateRecipe.class);
		ConfigurationSerialization.registerClass(CRBookCloneRecipe.class);
		ConfigurationSerialization.registerClass(CRFireworksRecipe.class);
		ConfigurationSerialization.registerClass(CRMapCloneRecipe.class);
		ConfigurationSerialization.registerClass(CRMapExtendRecipe.class);
		ConfigurationSerialization.registerClass(CRRepairRecipe.class);
		ConfigurationSerialization.registerClass(CRShieldDecorationRecipe.class);
		ConfigurationSerialization.registerClass(CRShulkerBoxDyeRecipe.class);
		ConfigurationSerialization.registerClass(CRTippedArrowRecipe.class);
		
		//furnace
		ConfigurationSerialization.registerClass(SimpleFurnaceRecipe.class);
		ConfigurationSerialization.registerClass(CRFurnaceRecipe.class);

		getCommand("migrateRecipes").setExecutor(new MigrateRecipesCommandExecutor(this));
		getCommand("addrecipe").setExecutor(new AddRecipeCommandExecutor(Collections.unmodifiableNavigableMap(adders)));
		getCommand("removerecipe").setExecutor(new RemoveRecipeCommandExecutor(Collections.unmodifiableNavigableMap(removers)));
		getCommand("listrecipes").setExecutor(new ListRecipesCommandExecutor(this::getRecipes,
				Collections.unmodifiableMap(recipeToItemMap),
				Collections.unmodifiableMap(recipeToCommandSenderDiplayMap)));

		getServer().getPluginManager().registerEvents(new ListRecipesListener(), this);

		
		//remove disabled vanilla recipes. only vanilla recipe types are shaped, shapeless and furnace
		for (File file : disabledFolder("furnace").listFiles()) {
			if (file.isDirectory()) continue;
			try {
				CRFurnaceRecipe furnaceRecipe = CRFurnaceRecipe.deserialzeFromNbt(NBTUtil.readNBTTagCompound(file), true);
				furnaceManager.removeVanillaRecipe(furnaceRecipe.getIngredient());
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Could not disable vanilla furnace recipe!", e);
			}
		}
		for (File file : disabledFolder("shaped").listFiles()) {
			if (file.isDirectory()) continue;
			try {
				CRShapedRecipe<ShapedRecipes> shapedRecipe = new CRShapedRecipe<>(NBTUtil.readNBTTagCompound(file));
				craftingManager.removeRecipe(shapedRecipe.getKey());
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Could not disable vanilla shaped recipe!", e);
			}
		}
		for (File file : disabledFolder("shapeless").listFiles()) {
			if (file.isDirectory()) continue;
			try {
				CRShapelessRecipe<ShapelessRecipes> shapelessRecipe = new CRShapelessRecipe<>(NBTUtil.readNBTTagCompound(file));
				craftingManager.removeRecipe(shapelessRecipe);
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Could not disable vanilla shapeless recipe!", e);
			}
		}
		
		//load custom recipes
		readers.forEach((type, reader) -> {
			File saveFolder = saveFolder(type);
			for (File recipeFile : saveFolder.listFiles()) {
				if (recipeFile.isDirectory()) continue;
				Recipe recipe = reader.apply(recipeFile);
				if (recipe instanceof CRCraftingRecipe) {
					@SuppressWarnings({ "rawtypes", "unchecked" })
					CRCraftingRecipe<IRecipe> craftingRecipe = (CRCraftingRecipe) recipe;
					craftingManager.addRecipe(craftingRecipe.getMinecraftKey(), craftingRecipe.getHandle(), craftingRecipe);
				} else if (recipe instanceof CRFurnaceRecipe) {
					CRFurnaceRecipe furnaceRecipe = (CRFurnaceRecipe) recipe;
					if (furnaceRecipe.isVanilla()) {
						furnaceManager.addVanillaRecipe(furnaceRecipe);
					} else {
						furnaceManager.addCustomRecipe(furnaceRecipe);
					}
				} else {
					getLogger().warning("Tried to read non-CR recipe: " + recipe);
				}
			}
		});
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
		String[] shape = bukkitRecipe.getShape();
		Map<Character, ItemStack> map = bukkitRecipe.getIngredientMap();
		List<? extends ChoiceIngredient> ingredients = Arrays.stream(shape)
				.flatMapToInt(s -> s.chars())
				.mapToObj(i -> map.getOrDefault((char) i, null))
				.map(SimpleChoiceIngredient::fromChoices)
				.collect(Collectors.toList());

		int width = shape[0].length();
		int height = shape.length;

		SimpleShapedRecipe simple = new SimpleShapedRecipe(bukkitRecipe.getResult(), width, height, ingredients);
		CraftingRecipe byKey = craftingManager.getRecipe(bukkitRecipe.getKey()); //can we do better? get by result and by ingredients?
		return simple.equals(byKey) ? (ShapedRecipe) byKey : simple;
	}

	@Override
	public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe) {
		List<? extends ChoiceIngredient> ingredients = bukkitRecipe.getIngredientList().stream()
				.map(SimpleChoiceIngredient::fromChoices)
				.collect(Collectors.toList());

		SimpleShapelessRecipe simple = new SimpleShapelessRecipe(bukkitRecipe.getResult(), ingredients);
		CraftingRecipe byKey = craftingManager.getRecipe(bukkitRecipe.getKey()); //can we do better? get by result and by ingredients?
		return simple.equals(byKey) ? (ShapelessRecipe) byKey : simple;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public FurnaceRecipe asCustomRecipesMirror(org.bukkit.inventory.FurnaceRecipe bukkitRecipe) {
		SimpleFurnaceRecipe simple = new SimpleFurnaceRecipe(bukkitRecipe.getInput(), bukkitRecipe.getResult(), bukkitRecipe.getExperience());

		CRFurnaceRecipe recipe = furnaceManager.getRecipe(bukkitRecipe.getInput());
		return simple.equals(recipe) ? recipe : simple;
	}


	public List<? extends Recipe> getRecipes(String type) {
		return recipeSuppliers.getOrDefault(type, Collections::emptyList).get();
	}

	@SuppressWarnings("unlikely-arg-type")
	public void save(String recipeType, String fileName, Recipe recipe) {
		File saveFolder = saveFolder(recipeType);

		File saveFile = new File(saveFolder, fileName);
		writers.get(recipeType).accept(recipe, saveFile);
	}

	public Recipe load(String recipeType, File saveFile) {
		return readers.get(recipeType).apply(saveFile);
	}

	public File saveFolder(String recipeType) {
		File folder = new File(getDataFolder(), recipeType);
		if (!folder.exists()) folder.mkdirs();
		return folder;
	}

	public File disabledFolder(String recipeType) {
		File saveFolder = saveFolder(recipeType);
		File disabledFolder = new File(saveFolder, "disabled");
		disabledFolder.mkdirs();
		return disabledFolder;
	}
	
	public void saveCraftingRecipeFile(String recipeType, CRCraftingRecipe<? extends IRecipe> recipe) {
		String fileName = craftingRecipeFileName(recipe);
		if (isVanillaCraftingRecipe(recipe.getMinecraftKey())) {
			File disabledFolder = disabledFolder(recipeType);
			File disabledRecipeFile = new File(disabledFolder, fileName);
			disabledRecipeFile.delete();
		} else {
			save(recipeType, fileName, recipe);
		}
	}
	
	public void saveFurnaceRecipeFile(CRFurnaceRecipe recipe) {
		String fileName = furnaceRecipeFileName(recipe);
		if (isVanillaFurnaceRecipe(recipe.getHandle().getIngredient(), recipe.getHandle().getResult())) {
			File disabledFolder = disabledFolder("furnace");
			File disabledRecipeFile = new File(disabledFolder, fileName);
			disabledRecipeFile.delete();
		} else {
			save("furnace", fileName, recipe);
		}
	}
	
	
	/**
	 * Delete recipe or disable vanilla recipe
	 * 
	 * @param recipeType shaped or shapeless
	 * @param recipe the vanilla recipe
	 */
	public void disableCraftingRecipeFile(String recipeType, CRCraftingRecipe recipe) {
		String fileName = craftingRecipeFileName(recipe);
		if (isVanillaCraftingRecipe(recipe.getMinecraftKey())) {
			
			//disable
			File disabledFolder = disabledFolder(recipeType);
			File disabledFile = new File(disabledFolder, fileName);
			try {
				NBTUtil.writeNBTTagCompound(disabledFile, recipe.serializeToNbt());
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Could not disable vanilla crafting recipe!", e);
			}
		} else {

			//delete
			File saveFolder = saveFolder(recipeType);
			File saveFile = new File(saveFolder, fileName);
			saveFile.delete();
		}
	}
	
	/**
	 * Delete recipe or disable vanilla recipe
	 * 
	 * @param recipe the furnace recipe
	 */
	public void disableFurnaceRecipeFile(CRFurnaceRecipe recipe) {
		String fileName = furnaceRecipeFileName(recipe);
		if (isVanillaFurnaceRecipe(recipe.getHandle().getIngredient(), recipe.getHandle().getResult())) {
			//disable
			File disabledFolder = disabledFolder("furnace");
			File disabledFile = new File(disabledFolder, fileName);
			try {
				NBTUtil.writeNBTTagCompound(disabledFile, recipe.serializeToNbt());
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Could not disable vanilla crafting recipe!", e);
			}
		} else {
			//delete
			File saveFolder = saveFolder("furnace");
			File saveFile = new File(saveFolder, fileName);
			saveFile.delete();
		}
	}
	
	public static String craftingRecipeFileName(CRCraftingRecipe<? extends IRecipe> recipe) {
		NamespacedKey key = recipe.getKey();
		return key.toString().replace(':', '_') + ".dat";
	}
	
	public static String furnaceRecipeFileName(CRFurnaceRecipe recipe) {
		return InventoryUtils.getItemName(recipe.getIngredient()) + '_' +
				InventoryUtils.getItemName(recipe.getResult()) + '_' +
				".dat";
	}


	public void setCraftingManager(CRCraftingManager craftingManager) {
		this.craftingManager = Objects.requireNonNull(craftingManager);
	}

	public void setFurnaceManager(CRFurnaceManager furnaceManager) {
		this.furnaceManager = Objects.requireNonNull(furnaceManager);
	}

	@SuppressWarnings("deprecation")
	public NamespacedKey getKey(String string) {
		String[] split = string.split(":");
		return split.length == 1 ? new NamespacedKey(this, string) : new NamespacedKey(split[0], split[1]);
	}

}
