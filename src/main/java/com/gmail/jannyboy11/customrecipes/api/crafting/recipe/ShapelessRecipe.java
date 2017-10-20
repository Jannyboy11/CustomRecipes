package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ToShapelessModifier;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

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
    public List<? extends CraftingIngredient> getIngredients();

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
    
    
    @Override
    public default <R extends CraftingRecipe> ModifiedCraftingRecipe<? extends CraftingRecipe> applyModifier(CraftingModifier<? super CraftingRecipe, R> modifier) {
        if (modifier instanceof ToShapelessModifier) {
            return applyShapelessModifier((ToShapelessModifier) modifier);
        } else {
            return CraftingRecipe.super.applyModifier(modifier);
        }
    }
    
    public default <R extends ShapelessRecipe> ModifiedShapelessRecipe applyShapelessModifier(ToShapelessModifier<? , R> modifier) {
        return new ModifiedShapelessRecipe() {
            private R modified = modifier.modify(ShapelessRecipe.this);
            
            @Override
            public ShapelessRecipe getBaseRecipe() {
                return ShapelessRecipe.this;
            }

            @Override
            public ToShapelessModifier<?, R> getModifier() {
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
            public String getGroup() {
                return modified.getGroup();
            }

            @Override
            public NamespacedKey getKey() {
                return modified.getKey();
            }            
        };
    }

}
