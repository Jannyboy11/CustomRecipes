package com.gmail.jannyboy11.customrecipes.api.furnace.recipe.simple;

import java.util.Objects;
import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

/**
 * Represents a furnace recipe with simple getters and setters for each method.
 * 
 * @author Jan
 */
public class SimpleFurnaceRecipe implements FurnaceRecipe {
    
    private final NamespacedKey key;
    private Ingredient ingredient;
    private Function<? super ItemStack, ? extends ItemStack> result;
    private Function<? super ItemStack, ? extends Float> experience;
    
    /**
     * Instantiates this simple furnace recipe using the provided ingredient, result function, experience function and key.
     * 
     * @param key the key
     * @param ingredient the ingredient
     * @param result the result function
     * @param experience the experience function
     */
    public SimpleFurnaceRecipe(NamespacedKey key, Ingredient ingredient,
            Function<? super ItemStack, ? extends ItemStack> result,
            Function<? super ItemStack, ? extends Float> experience) {
        this.key = Objects.requireNonNull(key, "key cannot be null");
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient cannot be null");
        this.result = Objects.requireNonNull(result, "result cannot be null");
        this.experience = Objects.requireNonNull(experience, "experience cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isIngredient(ItemStack input) {
        return ingredient.isIngredient(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack smelt(ItemStack input) {
        return result.apply(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float experienceReward(ItemStack input) {
        return experience.apply(input);
    }
    
    /**
     * Sets the ingredient.
     * 
     * @param ingredient the ingredient used by {@link SimpleFurnaceRecipe#isIngredient(ItemStack)}
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient cannot be null");
    }
    
    /**
     * Sets the result function.
     * 
     * @param resultFunction the result function used by {@link SimpleFurnaceRecipe#smelt(ItemStack)}
     */
    public void setResult(Function<? super ItemStack, ? extends ItemStack> resultFunction) {
        this.result = Objects.requireNonNull(resultFunction, "resultFunction cannot be null");
    }
    
    /**
     * Sets the experience function.
     * 
     * @param experienceFunction the experience function used by {@link SimpleFurnaceRecipe#experienceReward(ItemStack)}
     */
    public void setExperience(Function<? super ItemStack, ? extends Float> experienceFunction) {
        this.experience = Objects.requireNonNull(experienceFunction, "experienceFunction cannot be null");
    }

    /**
     * Builder method
     * 
     * @param ingredient the ingredient of the new furnace recipe
     * @return a new SimpleFurnaceRecipe instance
     */
    public SimpleFurnaceRecipe ingredient(Ingredient ingredient) {
        return new SimpleFurnaceRecipe(key, ingredient, result, experience);
    }
    
    /**
     * Builder method
     * 
     * @param resultFunction the result of the new furnace recipe
     * @return a new SimpleFurnaceRecipe instance
     */
    public SimpleFurnaceRecipe result(Function<? super ItemStack, ? extends ItemStack> resultFunction) {
        return new SimpleFurnaceRecipe(key, ingredient, resultFunction, experience);
    }
    
    /**
     * Builder method
     * 
     * @param experienceFunction the experience reward of the new furnace recipe
     * @return a new SimpleFurnaceRecipe instance
     */
    public SimpleFurnaceRecipe experience(Function<? super ItemStack, ? extends Float> experienceFunction) {
        return new SimpleFurnaceRecipe(key, ingredient, result, experienceFunction);
    }
}
