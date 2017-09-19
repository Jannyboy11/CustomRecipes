package com.gmail.jannyboy11.customrecipes.api.furnace.recipe;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.SimpleChoiceIngredient;

/**
 * Represents a furnace recipe with fixed ingredients, results and experience.
 * 
 * @author Jan
 */
public interface FixedFurnaceRecipe extends Recipe, FurnaceRecipe, ConfigurationSerializable {
    
    /**
     * Get the output of this furnace recipe.
     * 
     * @return the output
     */
    public ItemStack getResult();
    
    /**
     * Get the ingredient that this recipe accepts.
     * 
     * @return the ingredient ItemStack.
     */
    public ItemStack getIngredient();
    
    /**
     * Get the experience reward for this recipe.
     * 
     * @return the experience
     */
    public float getExperience();
    
    /**
     * Tests whether the input ItemStack is accepted by this recipe.
     * The default implementation returns whether input is accepted by the ingredient ItemStack using {@link SimpleChoiceIngredient#isIngredient(ItemStack)}.
     * 
     * @return true if the input is a valid ingredient for this recipe.
     */
    @Override
    public default boolean isIngredient(ItemStack input) {
        return SimpleChoiceIngredient.fromChoices(getIngredient()).isIngredient(input);
    }
    
    /**
     * Computes the resulting ItemStack for the input.
     * The default implementation returns the ItemStack returned by {@link FixedFurnaceRecipe#getResult()}
     * 
     * @return the molten item
     */
    @Override
    public default ItemStack smelt(ItemStack input) {
        return getResult();
    }

    /**
     * Gets the experience reward for the input.
     * The default implementation return the experience returnd by {@link FixedFurnaceRecipe#getExperience()}
     * 
     * @return the experience
     */
    @Override
    public default float experienceReward(ItemStack input) {
        return getExperience();
    }
    
    /**
     * Serializes this fixed furnace recipe.
     * <br>
     * The map contents 
     */
    @Override
    public default Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", new SerializableKey(getKey()));
        map.put("ingredient", getIngredient());
        map.put("result", getResult());
        float xp = getExperience();
        if (xp > 0F) map.put("experience", xp);
        return map;
    }

}
