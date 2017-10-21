package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ToShapelessModifier;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

//TODO remove ConfigurationSerializable?

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
     * {@inheritDoc}
     */
    @Override
    public default boolean matches(CraftingInventory craftingInventory, World world) {
        final List<CraftingIngredient> ingredients = new ArrayList<>(getIngredients());
        final List<ItemStack> contents = Arrays.asList(craftingInventory.getMatrix())
                .stream().filter(i -> !InventoryUtils.isEmptyStack(i))
                .collect(Collectors.toList());
        
        for (ItemStack stack : contents) {
            boolean match = false;
            for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ingredientIndex++) {
                CraftingIngredient ingredient = ingredients.get(ingredientIndex);
                if (ingredient.isIngredient(stack)) {
                    ingredients.remove(ingredientIndex);
                    match = true;
                    break;
                }
            }
            
            //there was no matching ingredient for the current ItemStack
            if (!match) return false;
        }
        
        //return true if there are no unused ingredients left over
        return ingredients.isEmpty();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public default List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
        ItemStack[] matrix = craftingInventory.getMatrix();
        final List<CraftingIngredient> ingredients = new ArrayList<>(getIngredients());
        
        List<ItemStack> leftOver = new ArrayList<>(matrix.length);
        
        for (int i = 0; i < matrix.length; i++) {
            ItemStack stack = matrix[i];
            ItemStack remainder = stack;

            for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ingredientIndex++) {
                CraftingIngredient ingredient = ingredients.get(ingredientIndex);
                if (ingredient.isIngredient(stack)) {
                    ingredients.remove(ingredientIndex); //shrink ingredients copy
                    
                    remainder = ingredient.getRemainder(stack);
                    
                    break;
                }
            }
            
            leftOver.add(remainder);
        }
        
        
        craftingInventory.setContents(leftOver.toArray(new ItemStack[matrix.length]));
        return leftOver;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public default boolean isHidden() {
        return false;
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
                "group", getGroup());
    }
    
    @Override
    public default Class<? extends ModifiedShapelessRecipe> getModifiedType() {
        return ModifiedShapelessRecipe.class;
    }
    
    @Override
    public default <R extends CraftingRecipe> ModifiedShapelessRecipe applyModifier(CraftingModifier<? extends CraftingRecipe, R> modifier) {
        return (ModifiedShapelessRecipe) CraftingRecipe.super.applyModifier(modifier);
    }
    
    public default <R extends ShapelessRecipe> ModifiedShapelessRecipe applyShapelessModifier(ToShapelessModifier<? extends ShapelessRecipe , R> modifier) {
        return new ModifiedShapelessRecipe() {
            private R modified = modifier.modify(ShapelessRecipe.this);
            
            @Override
            public ShapelessRecipe getBaseRecipe() {
                return ShapelessRecipe.this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public ToShapelessModifier<? extends ShapelessRecipe, R> getModifier() {
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
