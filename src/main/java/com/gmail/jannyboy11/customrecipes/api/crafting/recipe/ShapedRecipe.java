package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ToShapedModifier;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

//TODO create method getShape()
//TODO remove ConfigurationSerializable

/**
 * Represents a shaped recipe.
 * @author Jan
 *
 */
public interface ShapedRecipe extends CraftingRecipe, ConfigurationSerializable {
    
    /**
     * Get the Shape of this shaped recipe
     * @return the shape
     */
    public Shape getShape();

    /**
     * Get the width of the recipe
     * @return the width
     */
    public default int getWidth() {
        return getShape().getWidth();
    }

    /**
     * Get the height of the recipe
     * @return the height
     */
    public default int getHeight() {
        return getShape().getHeight();
    }

    /**
     * Get the ingredients of the recipe.
     * The size of the list must be equal to getWidth() * getHeight();
     *
     * @return the ingredients
     */
    public default List<? extends CraftingIngredient> getIngredients() {
        return getShape().getIngredients();
    }

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
                "group", getGroup(),
                "width", getWidth(),
                "height", getHeight());
    }
    
    
    @Override
    public default <R extends CraftingRecipe> ModifiedCraftingRecipe<? extends CraftingRecipe> applyModifier(CraftingModifier<? super CraftingRecipe, R> modifier) {
        if (modifier instanceof ToShapedModifier) {
            return applyShapedModifier((ToShapedModifier) modifier);
        } else {
            return CraftingRecipe.super.applyModifier(modifier);
        }
    }
    
    public default <R extends ShapedRecipe> ModifiedShapedRecipe applyShapedModifier(ToShapedModifier<?, R> modifier) {
        return new ModifiedShapedRecipe() {
            R modified = modifier.modify(ShapedRecipe.this);

            @Override
            public ShapedRecipe getBaseRecipe() {
                return ShapedRecipe.this;
            }

            @Override
            public ToShapedModifier<?, R> getModifier() {
                return modifier;
            }

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
            public int getWidth() {
                return modified.getWidth();
            }

            @Override
            public int getHeight() {
                return modified.getHeight();
            }
            
            @Override
            public String getGroup() {
                return modified.getGroup();
            }

            @Override
            public Shape getShape() {
                return modified.getShape();
            }
        };
    }

}
