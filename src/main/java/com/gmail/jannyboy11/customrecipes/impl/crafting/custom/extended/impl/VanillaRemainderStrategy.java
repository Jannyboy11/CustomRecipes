package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.RemainderStrategy;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.Shape;

import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;

public abstract class VanillaRemainderStrategy<R extends ExtendedCraftingRecipe> implements RemainderStrategy<R> {
    
    private VanillaRemainderStrategy() {}
    
    public static final VanillaRemainderStrategy<ExtendedShapedRecipe> SHAPED = new VanillaRemainderStrategy<ExtendedShapedRecipe>() {

        @Override
        public NonNullList<ItemStack> getRemaining(ExtendedShapedRecipe recipe, InventoryCrafting craftingInventory) {
            int inventoryWidth = craftingInventory.j();
            int inventoryHeight = craftingInventory.i();
            
            Shape shape = recipe.getShape();
            
            final int maxAddX = inventoryWidth - shape.getWidth();
            final int maxAddY = inventoryHeight - shape.getHeight();
            
            for (int addX = 0; addX <= maxAddX; addX++) {
                for (int addY = 0; addY <= maxAddY; addY++) {
                    for (boolean mirrored : new boolean[] {false, true}) {
                        if (matrixMatch(craftingInventory, shape, addX, addY, mirrored)) {
                            
                            List<ItemStack> matrix = craftingInventory.getContents();
                            NonNullList<ItemStack> remaining = NonNullList.a(ItemStack.a, matrix.toArray(new ItemStack[matrix.size()]));
                            
                            for (int recipeY = 0; recipeY < shape.getHeight(); recipeY++) {
                                for (int recipeX = 0; recipeX < shape.getWidth(); recipeX++) {
                                    
                                    int gridX = mirrored ? addX + shape.getWidth() - 1 - recipeX : recipeX + addX;
                                    int gridY = addY + recipeY;
                                    
                                    ExtendedCraftingIngredient ingredient = shape.getIngredient(recipeX, recipeY);
                                    ItemStack input = craftingInventory.c(gridX, gridY);
                                    
                                    ItemStack remainder = ingredient.getRemainder(input);
                                    
                                    int matrixIndex = gridY * inventoryWidth + gridX;
                                    remaining.set(matrixIndex, remainder);
                                }
                            }

                            for (int i = 0; i < remaining.size(); i++) {
                                craftingInventory.setItem(i, remaining.get(i));
                            }
                            return remaining;
                        }
                    }
                }
            }
            
            return NonNullList.a(ItemStack.a, craftingInventory.getContents().toArray(new ItemStack[craftingInventory.getContents().size()]));
        }
        
        private boolean matrixMatch(InventoryCrafting craftingGrid, Shape shape, int addX, int addY, boolean mirrored) {
            Map<Character, ? extends ExtendedCraftingIngredient> ingredients = shape.getIngredientMap();
            String[] pattern = shape.getPattern();
            
            for (int y = 0; y < shape.getHeight(); y++) {
                for (int x = 0; x < shape.getWidth(); x++) {
                    
                    final int gridX = mirrored ? shape.getWidth() - 1 - x + addX : x + addX;
                    final int gridY = y + addY;
                                        
                    char key = pattern[y].charAt(x);
                    ExtendedCraftingIngredient ingredient = ingredients.get(key);
                    if (ingredient == null) ingredient = RecipeItemStackAdapter.EMPTY;
                    
                    ItemStack input = craftingGrid.c(gridX, gridY);
                    if (!ingredient.accepts(input)) return false;
                    
                }
            }
            
            return true;
        }
        
    };
    
    public static final VanillaRemainderStrategy<ExtendedShapelessRecipe> SHAPELESS = new VanillaRemainderStrategy<ExtendedShapelessRecipe>() {

        @Override
        public NonNullList<ItemStack> getRemaining(ExtendedShapelessRecipe recipe, InventoryCrafting craftingInventory) {
            List<ItemStack> matrix = craftingInventory.getContents();
            final List<ExtendedCraftingIngredient> ingredientsCopy = new ArrayList<>(recipe.getIngredients());
            NonNullList<ItemStack> leftOver = NonNullList.a(matrix.size(), ItemStack.a);
            
            for (int i = 0; i < matrix.size(); i++) {
                ItemStack stack = matrix.get(i);
                ItemStack remainder = stack;

                for (int ingredientIndex = 0; ingredientIndex < ingredientsCopy.size(); ingredientIndex++) {
                    ExtendedCraftingIngredient ingredient = ingredientsCopy.get(ingredientIndex);
                    if (ingredient.accepts(stack)) {
                        ingredientsCopy.remove(ingredientIndex); //shrink ingredients copy
                        
                        remainder = ingredient.getRemainder(stack);
                        
                        break;
                    }
                }
                
                leftOver.set(i, remainder);
            }
            
            
            for (int i = 0; i < leftOver.size(); i++) {
                craftingInventory.setItem(i, leftOver.get(i));
            }
            return leftOver;
        }
        
    };

}
