package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.material.MaterialData;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;

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
     * Get the ingredients of this recipe. The order of the ingredients may or may not be important depending the recipe type.
     *
     * @return the list of ingredients.
     */
    List<? extends CraftingIngredient> getIngredients();

    /**
     * Get the list of ItemStacks that remain in the crafting table after crafting.
     * The size of the list is the same as the the size of the ingredient inventory.
     * This method also takes out the items from the crafting inventory.
     *
     * @param craftingInventory the crafting inventory - either a 3x3 workbench inventory, or the 2x2 hand crafting inventory
     * @return the ItemStacks that are left over after crafting completed - can contain null or AIR ItemStacks
     */
    public default List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
        //only called when the recipe matched - update all itemstacks according to the 'vanilla' strategy
        ItemStack[] matrix = craftingInventory.getMatrix();
        List<ItemStack> leftOver = new ArrayList<>(matrix.length);
        
        for (int i = 0; i < matrix.length; i++) {
            ItemStack matrixStack = matrix[i];
            
            if (InventoryUtils.isEmptyStack(matrixStack)) {
                leftOver.add(null);
                continue;
            }
            
            ItemStack clone = matrixStack.clone();
            MaterialData ingredientLeftover = InventoryUtils.getIngredientRemainder(matrixStack.getData());
            if (ingredientLeftover.getItemType() != Material.AIR) {
                clone.setData(ingredientLeftover);
                leftOver.add(clone);           
                continue;
            }
            
            //ingredient remainder is AIR
            leftOver.add(null);
        }
        
        craftingInventory.setContents(leftOver.toArray(new ItemStack[matrix.length]));
        return leftOver;
    }

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
    public default boolean isGrouped() {
        String group = getGroup();
        return !(group == null || group.isEmpty());
    }
    

    //TODO document this
    public default <R extends CraftingRecipe> ModifiedCraftingRecipe<? extends CraftingRecipe> applyModifier(CraftingModifier<? super CraftingRecipe, R> modifier) {
        return new ModifiedCraftingRecipe<CraftingRecipe>() {
            R modified = modifier.modify(CraftingRecipe.this);

            @Override
            public boolean matches(CraftingInventory craftingInventory, World world) {
                return modified.matches(craftingInventory, world);
            }

            @Override
            public ItemStack craftItem(CraftingInventory craftingInventory) {
               return modified.craftItem(craftingInventory);
            }

            @Override
            public ItemStack getResult() {
                return modified.getResult();
            }

            @Override
            public List<? extends CraftingIngredient> getIngredients() {
                return modified.getIngredients();
            }

            @Override
            public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
                return modified.getLeftOverItems(craftingInventory);
            }

            @Override
            public boolean isHidden() {
                return modified.isHidden();
            }

            @Override
            public NamespacedKey getKey() {
                return modified.getKey();
            }
            
            @Override
            public String getGroup() {
                return modified.getGroup();
            }

            @Override
            public CraftingRecipe getBaseRecipe() {
                return CraftingRecipe.this;
            }

            @Override
            public CraftingModifier<? super CraftingRecipe, R> getModifier() {
                return modifier;
            }
        };
    }

}
