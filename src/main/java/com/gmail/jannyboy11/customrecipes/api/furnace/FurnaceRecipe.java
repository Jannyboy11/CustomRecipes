package com.gmail.jannyboy11.customrecipes.api.furnace;

import org.bukkit.Keyed;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.api.Representable;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

/**
 * Represents a furnace recipe.
 * 
 * @author Jan
 */
public interface FurnaceRecipe extends Recipe, Keyed, ConfigurationSerializable, Representable {

    /**
     * Get the ingredient of this furnace recipe.
     * 
     * @return the ingredient
     */
    public CraftingIngredient getIngredient();

    /**
     * Get the output of this furnace recipe.
     * 
     * @return the output
     */
    public ItemStack getResult();

    /**
     * Get the experience that is granted for using this furnace recipe.
     * 
     * @return the experienced
     */
    public float getXp();

    /**
     * Set the ingredient of this furnace recipe.
     * 
     * @param ingredient
     */
    public void setIngredient(CraftingIngredient ingredient);

    /**
     * Set the output of this furnace recipe.
     * 
     * @param result the output
     */
    public void setResult(ItemStack result);

    /**
     * Set the experience of this furnace recipe.
     * 
     * @param xp the experience
     */
    public void setXp(float xp);
    
    /**
     * Gets the representation. The default implementation returns the result ItemStack.
     * 
     * @return the representation - same as {@link FurnaceRecipe#getResult()}
     */
    public default ItemStack getRepresentation() {
        return getResult();
    }
    
}
