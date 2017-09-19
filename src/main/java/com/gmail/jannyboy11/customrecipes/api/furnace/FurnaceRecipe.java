package com.gmail.jannyboy11.customrecipes.api.furnace;

import org.bukkit.Keyed;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a furnace recipe.
 * 
 * @author Jan
 */
public interface FurnaceRecipe extends Keyed {
    
    /**
     * Checks whether this furnace recipe accepts the input ItemStack.
     * 
     * @param input the input ItemStack
     * @return true when this recipe accepts the input as an ingredient
     */
    public boolean isIngredient(ItemStack input);
    
    /**
     * Computes the result of this recipe when the input ItemStack is smelt.
     * 
     * @param input the input ItemStack
     * @return the molten ItemStack
     */
    public ItemStack smelt(ItemStack input);
    
    /**
     * Computes the experience reward when the input ItemStack is smelt.
     * 
     * @param input the input ItemStack
     * @return the experience reward
     */
    public float experienceReward(ItemStack input);
    
}
