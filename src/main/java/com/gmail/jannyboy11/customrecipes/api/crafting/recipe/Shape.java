package com.gmail.jannyboy11.customrecipes.api.crafting.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.simple.SimpleChoiceIngredient;
import com.google.common.base.Objects;

public class Shape implements ConfigurationSerializable {
    
    private final String[] pattern;
    private final Map<Character, ? extends CraftingIngredient> ingredients;
    
    public Shape(String[] pattern, Map<Character, ? extends CraftingIngredient> ingredients) {
        this.pattern = Arrays.copyOf(pattern, pattern.length);
        this.ingredients = new HashMap<>(ingredients);
    }
    
    public String[] getPattern() {
        return Arrays.copyOf(pattern, pattern.length);
    }
    
    public CraftingIngredient getIngredient(char key) {
        return ingredients.get(key);
    }
    
    public List<? extends CraftingIngredient> getIngredients() {
        List<CraftingIngredient> ingredients = new ArrayList<>(getWidth() * getHeight());
        for (String row : pattern) {
            for (int i = 0; i < row.length(); i++) {
                char c = row.charAt(i);
                CraftingIngredient ingredient = this.ingredients.get(c);
                if (ingredient == null) ingredient = SimpleChoiceIngredient.ACCEPTING_EMPTY;
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
    
    public int getWidth() {
        return pattern[0].length();
    }
    
    public int getHeight() {
        return pattern.length;
    }
    
    public char getCharacter(int x, int y) {
        return pattern[y].charAt(x);
    }
    
    public CraftingIngredient getIngredient(int x, int y) {
        return getIngredient(getCharacter(x, y));
    }
    
    public Map<Character, ? extends CraftingIngredient> getIngredientMap() {
        return Collections.unmodifiableMap(this.ingredients);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Shape)) return false;
        
        Shape that = (Shape) o;
        return Arrays.deepEquals(this.getPattern(), that.getPattern())
                && Objects.equal(this.getIngredientMap(), that.getIngredientMap());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getPattern(), getIngredientMap());
    }
    
    @Override
    public String toString() {
        return getClass().getName() + '{' +
                "pattern()=" + Arrays.toString(getPattern()) +
                ",ingredientMap()= " + getIngredientMap() +
                '}';
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of("pattern", getPattern(),
                "ingredientMap", getIngredientMap().entrySet().stream().collect(Collectors
                        .toMap(e -> e.getKey().toString(), e -> e.getValue())));
    }

}
