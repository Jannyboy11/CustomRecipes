package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.List;

import org.bukkit.Keyed;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

/**
 * Represents a crafting recipe.
 *
 * @author Jan
 *
 */
public interface CraftingRecipe extends Keyed, Recipe {

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
     * Get the ingredients of this recipe. The ordef or the ingredients may or may not be important depending the recipe type.
     *
     * @return the list of ingredients.
     */
    List<? extends Ingredient> getIngredients();

    /**
     * Get the list of ItemStacks that remain in the crafting table after crafting.
     * The size of the list is the same as the the size of the ingredient inventory.
     * This method also takes out the items from the crafting inventory.
     *
     * @param craftingInventory the crafting inventory - either a 3x3 workbench inventory, or the 2x2 hand crafting inventory
     * @return the ItemStacks that are left over after crafting completed - can contain null or AIR ItemStacks
     */
    public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory);

    /**
     * Tests whether the recipe is a special recipe that have multiple ingredient patterns.
     * These special recipes are not shown in the Recipe Book.
     * <br>
     * Vanilla examples include
     * <ul>
     *     <li>ArmorDyeRecipe</li>
     * 	   <li>BannerAddPatternRecipe</li>
     * 	   <li>BannerDuplicateRecipe</li>
     * 	   <li>BookCloneRecipe</li>
     * 	   <li>FireworksRecipe</li>
     * 	   <li>MapCloneRecipe</li>
     * 	   <li>MapExtendRecipe</li>
     * 	   <li>RepairRecipe</li>
     * 	   <li>ShieldDecorationRecipe</li>
     * 	   <li>ShulkerBoxDyeRecipe</li>
     * 	   <li>TippedArowRecipe</li>
     * </ul>
     *
     * @return whether the recipe is special
     */
    public boolean isHidden();

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
