package com.gmail.jannyboy11.customrecipes.commands;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.PermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit.CRPermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.CraftingManager;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.RecipesFurnace;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class MigrateRecipesCommandExecutor implements CommandExecutor {

	private int incr = 0;
	
	private final CustomRecipesPlugin plugin;

	public MigrateRecipesCommandExecutor(CustomRecipesPlugin plugin) {
		this.plugin = plugin;
	}
	
	private MinecraftKey nextCustomKey() {
		return new MinecraftKey("customrecipes", "migrated_" + String.valueOf(incr++));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		File pluginDataFolder = plugin.getDataFolder();

		if (!pluginDataFolder.exists()) {
			sender.sendMessage(ChatColor.GREEN + "Plugin data folder does not exists, no recipes were migrated.");
			return true;
		}

		File extraRecipesFolder = new File(pluginDataFolder, "extra_recipes");
		if (extraRecipesFolder.exists() && extraRecipesFolder.isDirectory()) {
			migrateExtraRecipes(sender, extraRecipesFolder);
		} else {
			sender.sendMessage(ChatColor.RED + "Could not migrate extra recipes, the extra_recipes file does not exist or is not a directory.");
		}

		File disabledRecipesFolder = new File(pluginDataFolder, "disabled_recipes");
		if (disabledRecipesFolder.exists() && disabledRecipesFolder.isDirectory()) {
			migrateDisabledRecipes(sender, disabledRecipesFolder);
		} else {
			sender.sendMessage(ChatColor.RED + "Could not migrate disabled recipes, the disabled_recipes file does not exist or is not a directory.");
		}

		sender.sendMessage(ChatColor.GREEN + "Migration complete! Restart your server for the migrated recipes to take effect.");

		return true;
	}


	private void migrateExtraRecipes(CommandSender sender, File extraFolder) {
		sender.sendMessage(ChatColor.YELLOW + "Migrating shaped recipes");
		//shaped
		File folder = new File(extraFolder, "shaped_recipes");
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);

					int width = readTag.getInt("Width");
					int height = readTag.getInt("Height");
					NBTTagList nbtIngredients = readTag.getList("Ingredients", NBTUtil.COMPOUND);
					NonNullList<RecipeItemStack> ingredients = NonNullList.a(width * height, RecipeItemStack.a);
					for (int i = 0; i < nbtIngredients.size(); i++) {
						NBTTagCompound ingredientTag = nbtIngredients.get(i);
						ItemStack ingredientStack = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
						RecipeItemStack ingredient = RecipeItemStack.a(new ItemStack[] {ingredientStack});
						ingredients.set(i, ingredient);
					}

					ShapedRecipes shaped = new ShapedRecipes("", height, height, ingredients, result);
					MinecraftKey key = nextCustomKey();
					shaped.setKey(key);

					CRShapedRecipe<ShapedRecipes> cr = new CRShapedRecipe<>(shaped);
					plugin.saveCraftingRecipeFile("shaped", cr);

				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating shaped recipe " + name + ", please check your console for errors.");
				}
			}
		}

		sender.sendMessage(ChatColor.YELLOW + "Migrating shapeless recipes");
		//shapeless
		folder = new File(extraFolder, "shapeless_recipes");
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);

					NBTTagList nbtIngredients = readTag.getList("Ingredients", NBTUtil.COMPOUND);
					NonNullList<RecipeItemStack> ingredients = NonNullList.a(nbtIngredients.size(), RecipeItemStack.a);
					for (int i = 0; i < nbtIngredients.size(); i++) {
						NBTTagCompound ingredientTag = nbtIngredients.get(i);
						ItemStack ingredientStack = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
						RecipeItemStack ingredient = RecipeItemStack.a(new ItemStack[] {ingredientStack});
						ingredients.set(i, ingredient);
					}

					ShapelessRecipes shaped = new ShapelessRecipes("", result, ingredients);
					MinecraftKey key = nextCustomKey();
					shaped.setKey(key);

					CRShapelessRecipe<ShapelessRecipes> cr = new CRShapelessRecipe<>(shaped);
					plugin.saveCraftingRecipeFile("shapeless", cr);

				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating shapeless recipe " + name + ", please check your console for errors.");
				}
			}
		}

		sender.sendMessage(ChatColor.YELLOW + "Migrating permission recipes");
		//permission
		folder = new File(extraFolder, "permission_recipes");
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);

					int width = readTag.getInt("Width");
					int height = readTag.getInt("Height");
					NBTTagList nbtIngredients = readTag.getList("Ingredients", NBTUtil.COMPOUND);
					NonNullList<RecipeItemStack> ingredients = NonNullList.a(width * height, RecipeItemStack.a);
					for (int i = 0; i < nbtIngredients.size(); i++) {
						NBTTagCompound ingredientTag = nbtIngredients.get(i);
						ItemStack ingredientStack = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
						RecipeItemStack ingredient = RecipeItemStack.a(new ItemStack[] {ingredientStack});
						ingredients.set(i, ingredient);
					}
					String permission = readTag.getString("Permission");

					PermissionRecipe shaped = new PermissionRecipe("", height, height, ingredients, result, permission);
					MinecraftKey key = nextCustomKey();
					shaped.setKey(key);

					CRPermissionRecipe cr = new CRPermissionRecipe(shaped);
					plugin.saveCraftingRecipeFile("permission", cr);

				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating permission recipe " + name + ", please check your console for errors.");
				}
			}
		}

		sender.sendMessage(ChatColor.YELLOW + "Migrating furnace recipes");
		//furnace
		folder = new File(extraFolder, "furnace_recipes");
		if (folder.exists() && folder.isDirectory()) {

			RecipesFurnace recipesFurnace = new RecipesFurnace();
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);
					NBTTagCompound ingredientTag = readTag.getCompound("Ingredient");
					ItemStack ingredient = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
					float xp = readTag.hasKeyOfType("Experience", NBTUtil.FLOAT) ? readTag.getFloat("Experience") : 0F;

					FurnaceRecipe furnaceRecipe = new FurnaceRecipe(recipesFurnace, recipesFurnace.customRecipes, recipesFurnace.customExperience, ingredient);
					furnaceRecipe.setResult(result);
					if (xp > 0) furnaceRecipe.setXp(xp);
					CRFurnaceRecipe cr = new CRFurnaceRecipe(furnaceRecipe);

					plugin.saveFurnaceRecipeFile(cr);
				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating furnace recipe " + name + ", please check your console for errors.");
				}
			}
		}

	}

	private void migrateDisabledRecipes(CommandSender sender, File disabledFolder) {	
		
		sender.sendMessage(ChatColor.YELLOW + "Migrating disabled shaped recipes");
		//shaped
		File folder = new File(disabledFolder, "shaped_recipes");
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);

					int width = readTag.getInt("Width");
					int height = readTag.getInt("Height");
					NBTTagList nbtIngredients = readTag.getList("Ingredients", NBTUtil.COMPOUND);
					NonNullList<ItemStack> ingredients = NonNullList.a(width * height, ItemStack.a);
					for (int i = 0; i < nbtIngredients.size(); i++) {
						NBTTagCompound ingredientTag = nbtIngredients.get(i);
						ItemStack ingredientStack = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
						ingredients.set(i, ingredientStack);
					}

					LegacyShapedRecipe legacyShaped = new LegacyShapedRecipe(width, height, result, ingredients);
					
					ShapedRecipes match = null;
					Iterator<IRecipe> iterator = CraftingManager.recipes.iterator();
					
					//loop until a match was found
					while (iterator.hasNext() && match == null) {
						IRecipe serverRecipe = iterator.next();
						if (serverRecipe instanceof ShapedRecipes) {
							ShapedRecipes shaped = (ShapedRecipes) serverRecipe;
							if (legacyShaped.equalsRecipe(shaped)) {
								match = shaped;
							}
						}
					}

					if (match != null) {
						
						//very nice! great success!
						CRShapedRecipe<ShapedRecipes> crShapedRecipe = new CRShapedRecipe<>(match);
						plugin.disableCraftingRecipeFile("shaped", crShapedRecipe);
						
					} else {
						sender.sendMessage(ChatColor.RED + "Couln't migrate disabled shaped recipe \"" + name + "\", no match was found in the crafting manager. Is it already disabled?");
					}

				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating disabled shaped recipe " + name + ", please check your console for errors.");
				}
			}
		}

		sender.sendMessage(ChatColor.YELLOW + "Migrating disabled shapeless recipes");
		//shapeless
		folder = new File(disabledFolder, "shapeless_recipes");
		if (folder.exists() && folder.isDirectory()) {
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);

					NBTTagList nbtIngredients = readTag.getList("Ingredients", NBTUtil.COMPOUND);
					NonNullList<ItemStack> ingredients = NonNullList.a(nbtIngredients.size(), ItemStack.a);
					for (int i = 0; i < nbtIngredients.size(); i++) {
						NBTTagCompound ingredientTag = nbtIngredients.get(i);
						ItemStack ingredientStack = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
						ingredients.set(i, ingredientStack);
					}

					LegacyShapelessRecipe legacyShapeless = new LegacyShapelessRecipe(result, ingredients);

					ShapelessRecipes match = null;
					Iterator<IRecipe> iterator = CraftingManager.recipes.iterator();
					
					//loop until a match was found
					while (iterator.hasNext() && match == null) {
						IRecipe serverRecipe = iterator.next();
						if (serverRecipe instanceof ShapelessRecipes) {
							ShapelessRecipes shapeless = (ShapelessRecipes) serverRecipe;
							if (legacyShapeless.equalsRecipe(shapeless)) {
								match = shapeless;
							}
						}
					}

					if (match != null) {
						
						//very nice! great success!
						CRShapelessRecipe<ShapelessRecipes> crShapedRecipe = new CRShapelessRecipe<>(match);
						plugin.disableCraftingRecipeFile("shaped", crShapedRecipe);
						
					} else {
						sender.sendMessage(ChatColor.RED + "Couln't migrate disabled shapeless recipe \"" + name + "\", no match was found in the crafting manager. Is it already disabled?");
					}

				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating disabled shapeless recipe " + name + ", please check your console for errors.");
				}
			}
		}

		//no disabled permission recipes
		
		sender.sendMessage(ChatColor.YELLOW + "Migrating disabled furnace recipes");
		//furnace
		folder = new File(disabledFolder, "furnace_recipes");
		if (folder.exists() && folder.isDirectory()) {

			RecipesFurnace recipesFurnace = new RecipesFurnace();
			for (File file : folder.listFiles()) {
				String name = file.getName();

				try {
					NBTTagCompound readTag = NBTUtil.readNBTTagCompound(file);
					NBTTagCompound resultTag = readTag.getCompound("Result");
					ItemStack result = resultTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(resultTag);
					NBTTagCompound ingredientTag = readTag.getCompound("Ingredient");
					ItemStack ingredient = ingredientTag.isEmpty() ? ItemStack.a : NBTUtil.deserializeItemStack(ingredientTag);
					float xp = readTag.hasKeyOfType("Experience", NBTUtil.FLOAT) ? readTag.getFloat("Experience") : 0F;

					FurnaceRecipe furnaceRecipe = new FurnaceRecipe(recipesFurnace, recipesFurnace.customRecipes, recipesFurnace.customExperience, ingredient);
					furnaceRecipe.setResult(result);
					if (xp > 0) furnaceRecipe.setXp(xp);
					CRFurnaceRecipe cr = new CRFurnaceRecipe(furnaceRecipe, true); //true because CustomRecipes always used the vanilla maps, because the custom one didn't support xp back in 1.8.

					plugin.disableFurnaceRecipeFile(cr);
				} catch (IOException e) {
					e.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Something went wrong when migrating disabled furnace recipe " + name + ", please check your console for errors.");
				}
			}
		}

	}
	
	
	//shaped recipe as in minecraft 1.11 and earlier versions. ingredients was just a list of itemstacks
	private static class LegacyShapedRecipe {
		private int width;
		private int heigth;
		private ItemStack result;
		private NonNullList<ItemStack> ingredients;
		
		public LegacyShapedRecipe(int width, int heigth, ItemStack result, NonNullList<ItemStack> ingredients) {
			this.width = width;
			this.heigth = heigth;
			this.result = result;
			this.ingredients = ingredients;
		}
		
		public boolean equalsRecipe(ShapedRecipes oneTwelveVersion) {
			int width = (int) ReflectionUtil.getDeclaredFieldValue(oneTwelveVersion, "width");
			if (width != this.width) return false;
			
			int heigth = (int) ReflectionUtil.getDeclaredFieldValue(oneTwelveVersion, "height");
			if (heigth != this.heigth) return false;
			
			ItemStack result = (ItemStack) ReflectionUtil.getDeclaredFieldValue(oneTwelveVersion, "result");
			if (!ItemStack.fastMatches(result, this.result)) return false;
			
			NonNullList<RecipeItemStack> ingredients = (NonNullList<RecipeItemStack>) ReflectionUtil.getDeclaredFieldValue(oneTwelveVersion, "items");
			for (int i = 0; i < ingredients.size(); i++) {
				RecipeItemStack ingredient = ingredients.get(i);
				if (!ingredient.a(this.ingredients.get(i))) return false; //legacy ingredient was not accepted by the 1.12 ingredient
			}
			
			return true;
		}
	}
	
	//shapeless recipe as in minecraft 1.11 and earlier versions
	private static class LegacyShapelessRecipe {
		private ItemStack result;
		private NonNullList<ItemStack> ingredients;
		
		public LegacyShapelessRecipe(ItemStack result, NonNullList<ItemStack> ingredients) {
			this.result = result;
			this.ingredients = ingredients;
		}
		
		public boolean equalsRecipe(ShapelessRecipes oneTwelveVersion) {
			ItemStack result = (ItemStack) ReflectionUtil.getDeclaredFieldValue(oneTwelveVersion, "result");
			if (!ItemStack.fastMatches(result, this.result)) return false;
			
			NonNullList<RecipeItemStack> ingredients = (NonNullList<RecipeItemStack>) ReflectionUtil.getDeclaredFieldValue(oneTwelveVersion, "ingredients");
			for (int i = 0; i < ingredients.size(); i++) {
				RecipeItemStack ingredient = ingredients.get(i);
				if (!ingredient.a(this.ingredients.get(i))) return false; //legacy ingredient was not accepted by the 1.12 ingredient
			}
			
			return true;
		}
	}
	
	

}
