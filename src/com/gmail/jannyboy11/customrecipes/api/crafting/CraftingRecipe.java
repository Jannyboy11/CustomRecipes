package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.List;

import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a crafting recipe
 * 
 * @author Jan
 *
 */
public interface CraftingRecipe {
	
	/**
	 * Tests whether the items in the crafting recipe match to a crafting recipe.
	 * 
	 * @param craftingInventory the crafting inventory - either a 3x3 workbench inventory, or the 2x2 hand crafting inventory
	 * @param world the world where crafting takes place
	 * @return whether there is a match to a crafting recipe
	 */
	public boolean matches(CraftingInventory craftingInventory, World world);
	
	/**
	 * Get the ItemStack that will be put in the result slot of the crafting inventory.
	 * 
	 * @param craftingInventory the crafting inventory - either a 3x3 workbench inventory, or the 2x2 hand crafting inventory
	 * @return the crafting result ItemStack
	 */
	public ItemStack craftItem(CraftingInventory craftingInventory);
	
	/**
	 * Get the result of this recipe. This is NOT the item that is used by the recipe when the player crafts an item.
	 * See {@link com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe#craftItem}
	 * 
	 * @return the result ItemStack
	 */
	public ItemStack getResult();
	
	/**
	 * Get the list of ItemStacks that remain in the crafting table after crafting.
	 * 
	 * @param craftingInventory the crafting inventory - either a 3x3 workbench inventory, or the 2x2 hand crafting inventory
	 * @return the ItemStacks that are left over after crafting completed
	 */
	public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory);

	/**
	 * Tests whether the recipe is a special recipe that have multiple ingredient patterns.
	 * These special recipes are not shown in the Recipe Book.
	 * 
	 * Vanilla examples include
	 * 		- ArmorDyeRecipe
	 * 		- BannerAddPatternRecipe
	 * 		- BannerDuplicateRecipe
	 * 		- BookCloneRecipe
	 * 		- FireworksRecipe
	 * 		- MapCloneRecipe
	 * 		- MapExtendRecipe
	 * 		- RepairRecipe
	 * 		- ShieldDecorationRecipe
	 * 		- ShulkerBoxDyeRecipe
	 * 		- TippedArowRecipe
	 * 
	 * @return whether the recipe is special
	 */
	public boolean isHidden();

	/**
	 * Get the group of the recipe. Groups are used for grouping recipes together in the Recipe Book.
	 * Examples of vanilla recipes with a group are recipes that take different kinds of wood, or different colors of wool.
	 * 
	 * 
	 * 
	 * @return the group identifier, or the empty string if the recipe has no group
	 */
	public String getGroup();

}
