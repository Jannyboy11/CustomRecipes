package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.SerializableKey;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ModifiedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.ToShapedModifier;
import com.gmail.jannyboy11.customrecipes.api.util.GridView;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

//TODO remove ConfigurationSerializable?

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
     * {@inheritDoc}
     */
    @Override
    public default boolean matches(CraftingInventory craftingInventory, World world) {
        int width, height;
        
        //check boundaries for the crafting inventory
        InventoryType type = craftingInventory.getType();
        switch(type) {
            case CRAFTING:
                width = height = 2;
                break;
            case WORKBENCH:
                width = height = 3;
                break;
            default: return false; //unknown crafting inventory type.
        }
        
        GridView gridInventory = new GridView(craftingInventory, width, height);
        Shape shape = getShape();
        
        final int maxAddX = width - shape.getWidth();
        final int maxAddY = height - shape.getHeight();
        
        for (int addX = 0; addX <= maxAddX; addX++) {
            for (int addY = 0; addY <= maxAddY; addY++) {
                if (matrixMatch(gridInventory, addX, addY, true)
                        || matrixMatch(gridInventory, addX, addY, false)) {
                    return true;
                }
            }
        }
                
        return false;
    }
    
    private boolean matrixMatch(GridView craftingInventory, int addX, int addY, boolean mirrored) {
        Shape shape = getShape();
        
        Map<Character, ? extends CraftingIngredient> ingredients = shape.getIngredientMap();
        String[] pattern = shape.getPattern();
        
        for (int shapeY = 0; shapeY < shape.getHeight(); shapeY++) {
            for (int shapeX = 0; shapeX < shape.getWidth(); shapeX++) {
                
                final int gridX = addX + (mirrored ? shape.getWidth() - 1 - shapeX : shapeX);
                final int gridY = addY + shapeY;
                                    
                char key = pattern[shapeY].charAt(shapeX);
                CraftingIngredient ingredient = ingredients.get(key);
                if (ingredient == null) ingredient = InventoryUtils::isEmptyStack;
                
                ItemStack input = craftingInventory.getItem(gridX, gridY);
                if (!ingredient.isIngredient(input)) return false;
            }
        }
      
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public default List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
        int inventoryWidth, inventoryHeight;
        
        //check boundaries for the crafting inventory
        InventoryType type = craftingInventory.getType();
        switch(type) {
            case CRAFTING:
                inventoryWidth = inventoryHeight = 2;
                break;
            case WORKBENCH:
                inventoryWidth = inventoryHeight = 3;
                break;
            
            //unknown recipe type, fall back
            default: return CraftingRecipe.super.getLeftOverItems(craftingInventory);
        }
        
        GridView gridInventory = new GridView(craftingInventory, inventoryWidth, inventoryHeight);
        Shape shape = getShape();
        
        final int maxAddX = inventoryWidth - shape.getWidth();
        final int maxAddY = inventoryHeight - shape.getHeight();
        
        for (int addX = 0; addX <= maxAddX; addX++) {
            for (int addY = 0; addY <= maxAddY; addY++) {
                for (boolean mirrored : new boolean[] {false, true}) {
                    if (matrixMatch(gridInventory, addX, addY, mirrored)) {
                        
                        ItemStack[] matrix = craftingInventory.getMatrix();
                        List<ItemStack> remaining = new ArrayList<>(List.of(matrix));
                        
                        for (int recipeY = 0; recipeY < shape.getHeight(); recipeY++) {
                            for (int recipeX = 0; recipeX < shape.getWidth(); recipeX++) {
                                
                                int gridX = mirrored ? addX + shape.getWidth() - 1 - recipeX : recipeX + addX;
                                int gridY = addY + recipeY;
                                
                                CraftingIngredient ingredient = shape.getIngredient(recipeX, recipeY);
                                ItemStack input = gridInventory.getItem(gridX, gridY);
                                
                                ItemStack remainder = ingredient.getRemainder(input);
                                
                                int matrixIndex = gridInventory.getIndex(gridX, gridY);
                                remaining.set(matrixIndex, remainder);
                            }
                        }
                        
                        craftingInventory.setMatrix(remaining.toArray(new ItemStack[matrix.length]));
                        return remaining;
                    }
                }
            }
        }

        //fallback
        return CraftingRecipe.super.getLeftOverItems(craftingInventory);
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
                "group", getGroup(),
                "width", getWidth(),
                "height", getHeight());
    }
    
    
    @Override
    public default Class<? extends ModifiedShapedRecipe> getModifiedType() {
        return ModifiedShapedRecipe.class;
    }
    
    @Override
    public default <R extends CraftingRecipe> ModifiedShapedRecipe applyModifier(CraftingModifier<? extends CraftingRecipe, R> modifier) {
        return (ModifiedShapedRecipe) CraftingRecipe.super.applyModifier(modifier);
    }
    
    public default <R extends ShapedRecipe> ModifiedShapedRecipe applyShapedModifier(ToShapedModifier<? extends ShapedRecipe, R> modifier) {
        return new ModifiedShapedRecipe() {
            R modified = modifier.modify(ShapedRecipe.this);

            @Override
            public ShapedRecipe getBaseRecipe() {
                return ShapedRecipe.this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public ToShapedModifier<? extends ShapedRecipe, R> getModifier() {
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
