package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl.RecipeItemStackAdapter;

import net.minecraft.server.v1_12_R1.NonNullList;

public interface Shape {
    
    public String[] getPattern();
    
    public Map<Character, ? extends ExtendedCraftingIngredient> getIngredientMap();
    
    public default NonNullList<? extends ExtendedCraftingIngredient> getIngredients() {
        Map<Character, ? extends ExtendedCraftingIngredient> map = getIngredientMap();
        
        return Arrays.stream(getPattern()).flatMap(s -> s.chars().boxed())
                .map(i -> (char) i.intValue())
                .map(c -> getIngredient(c, map))
                .collect(Collectors.toCollection(NonNullList::a));
    }
    
    public default ExtendedCraftingIngredient getIngredient(char key) {
        return getIngredient(key, getIngredientMap());
    }
    
    private static ExtendedCraftingIngredient getIngredient(char key, Map<Character, ? extends ExtendedCraftingIngredient> ingredients) {
        ExtendedCraftingIngredient ingr = ingredients.get(key);
        return ingr == null ? RecipeItemStackAdapter.EMPTY : ingr; 
    }
    
    public default int getWidth() {
        return getPattern()[0].length();
    }
    
    public default int getHeigth() {
        return getPattern().length;
    }

}
