package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.MatchStrategy;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.Shape;

import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.World;

public abstract class VanillaMatchStrategy<R extends ExtendedCraftingRecipe> implements MatchStrategy<R> {
    
    private VanillaMatchStrategy() {}
    
    public static final VanillaMatchStrategy<ExtendedShapedRecipe> SHAPED = new VanillaMatchStrategy<ExtendedShapedRecipe>() {

        @Override
        public boolean matches(ExtendedShapedRecipe recipe, InventoryCrafting inventoryCrafting, World world) {            
            int inventoryHeight = inventoryCrafting.i();
            int inventoryWidth = inventoryCrafting.j();
            
            Shape shape = recipe.getShape();
            int maxAddX = inventoryWidth - shape.getWidth();
            int maxAddY = inventoryHeight - shape.getHeigth();
            
            for (int addX = 0; addX <= maxAddX; addX++) {
                for (int addY = 0; addY <= maxAddY; addY++) {
                    for (boolean mirrored : new boolean[] {false, true}) {
                        if (matrixMatch(inventoryCrafting, shape, addX, addY, mirrored)) return true;
                    }
                }
            }
            
            return false;
        }
        
        private boolean matrixMatch(InventoryCrafting craftingGrid, Shape shape, int addX, int addY, boolean mirrored) {
            Map<Character, ? extends ExtendedCraftingIngredient> ingredients = shape.getIngredientMap();
            String[] pattern = shape.getPattern();
            
            for (int y = 0; y < shape.getHeigth(); y++) {
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
    
    public static final VanillaMatchStrategy<ExtendedShapelessRecipe> SHAPELESS = new VanillaMatchStrategy<ExtendedShapelessRecipe>() {

        @Override
        public boolean matches(ExtendedShapelessRecipe recipe, InventoryCrafting inventory, World world) {
            ArrayList<ExtendedCraftingIngredient> ingredientsCopy = new ArrayList<>(recipe.getIngredients());
            
            for (int y = 0; y < inventory.i(); y++) {
                iterateX:
                for (int x = 0; x < inventory.j(); x++) {
                    
                    ItemStack input = inventory.c(x, y);
                    
                    if (!input.isEmpty()) {
                        ListIterator<ExtendedCraftingIngredient> iterator = ingredientsCopy.listIterator();
                        
                        while (iterator.hasNext()) {
                            ExtendedCraftingIngredient ingredient = iterator.next();
                            if (ingredient.accepts(input)) {
                                iterator.remove();
                                continue iterateX;
                            }
                        }
                        
                        return false;
                    }
                }
            }
            
            return true;
        }
        
    };

}
