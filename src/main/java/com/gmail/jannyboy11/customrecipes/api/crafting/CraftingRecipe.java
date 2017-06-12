package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.api.Representable;

/**
 * Represents a crafting recipe.
 * 
 * @author Jan
 *
 */
public interface CraftingRecipe extends Keyed, Representable, Recipe {
	
	/**
	 * Tests whether the items in the crafting inventory match to this crafting recipe.
	 * 
	 * @param craftingInventory the crafting inventory - either a 3x3 workbench inventory, or the 2x2 hand crafting inventory
	 * @param world the world in which crafting takes place
	 * @return the recipe accepts the inventory and world as valid input for the result ItemStack
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
	 * Get the key of the recipe.
	 * 
	 * @return the key, which may be randomly generated if no key was present.
	 */
	public NamespacedKey getKey();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public default ItemStack getRepresentation() {
		ItemStack result = getResult();
		
		ItemStack representation = (result == null || result.getType() == Material.AIR) ? new ItemStack(Material.AIR) : result.clone();
		if (representation.getType() == Material.AIR) return null;
		
		ItemMeta meta = representation.getItemMeta();

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "Hidden: " + isHidden());
		
		meta.setDisplayName(ChatColor.GRAY + getKey().toString());
		meta.setLore(lore);
		
		representation.setItemMeta(meta);
		return representation;
	}
	
	/**
	 * Get the group of the recipe. Groups are used for grouping recipes together in the Recipe Book.
	 * Examples of vanilla recipes with a group are recipes that take different kinds of wood, or different colors of wool.
	 * The recipe book display mechanic is client side only, <a href="https://twitter.com/dinnerbone/status/856505341479145472">but is subject to change in Minecraft 1.13</a>.
	 * 
	 * @return the group identifier, or the empty string if the recipe has no group
	 */
	public default String getGroup() {
		return "";
	}
	
	/**
	 * Check whether this recipe has a group.
	 * 
	 * @return true if the shaped recipe has a group, otherwise false
	 */
	public default boolean hasGroup() {
		String group = getGroup();
		return !(group == null || group.isEmpty());
	}

}
