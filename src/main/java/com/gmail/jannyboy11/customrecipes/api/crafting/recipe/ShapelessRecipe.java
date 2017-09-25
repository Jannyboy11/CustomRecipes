package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a shapeless recipe.
 * @author Jan
 *
 */
public interface ShapelessRecipe extends CraftingRecipe, ConfigurationSerializable {

    /**
     * Get the ingredients of the recipe.
     *
     * @return the ingredients
     */
    public List<? extends ChoiceIngredient> getIngredients();

    /**
     * Serializes this ShapedRecipe.
     *
     * @see org.bukkit.configuration.serialization.ConfigurationSerializable
     * @see org.bukkit.configuration.serialization.ConfigurationSerialization
     *
     * @return a map containing serialized fields
     */
    public default Map<String, Object> serialize() {
        return Map.of("key", new SerializableKey(getKey()),
                "ingredients", getIngredients(),
                "result", getResult(),
                "hidden", isHidden(),
                "group", getGroup());
    }

}
