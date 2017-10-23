package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.mojang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

//TODO create a wrapper that implements ExtendedShapedRecipe
public class MojangShapedRecipe extends ShapedRecipes implements MojangCraftingRecipe {
    
    private final String group;
    private final int width, height;
    
    private Shape shape; //lazy
    
    public MojangShapedRecipe(String group, int width, int height,
            NonNullList<RecipeItemStack> ingredients, ItemStack result) {
        super(group, width, height, ingredients, result);
        this.group = group;
        this.width = width;
        this.height = height;
    }

    public MojangShapedRecipe(String group, ItemStack result, Shape shape) {
        this(group, shape.getWidth(), shape.getHeight(), shape.getIngredients(), result);
        
        this.shape = shape;
    }

    public String getGroup() {
        return group;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Shape getShape() {
        return shape == null ? shape = createShape() : shape;
    }
    
    private Shape createShape() {
        NonNullList<RecipeItemStack> ingredients = this.d();
        
        List<List<Character>> patternList = new ArrayList<>();
        Map<Character, RecipeItemStack> keys = new HashMap<>();
        
        Iterator<RecipeItemStack> iterator = ingredients.iterator();
        char c = 'a';
        for (int i = 0; i < height; i++) {
            List<Character> inner = new ArrayList<>();
            patternList.add(inner);
            for (int j = 0; j < width; j++) {
                char key = c;
                
                RecipeItemStack ingredient = iterator.next();
                if (ingredient == RecipeItemStack.a) key = ' ';
                
                inner.add(key);
                keys.put(key, ingredient);
                
                c++;
            }
        }
        
        String[] pattern = new String[patternList.size()];
        for (int i = 0; i < pattern.length; i++) {
            char[] chars = new char[patternList.size()];
            for (int j = 0; j < patternList.size(); j++) {
                chars[j] = patternList.get(i).get(j).charValue();
            }
            pattern[i] = new String(chars);
        }
        
        return new Shape(pattern, keys);
    }

    public static class Shape {
        private final String[] pattern;
        private Map<Character, RecipeItemStack> keys = new HashMap<>();
        
        public Shape(String[] pattern, Map<Character, RecipeItemStack> keys) {
            this.pattern = pattern;
            this.keys.putAll(keys);
            this.keys.put(' ', RecipeItemStack.a);
        }
        
        public String[] getPattern() {
            return Arrays.copyOf(pattern, pattern.length);
        }
        
        public Map<Character, RecipeItemStack> getKeys() {
            return new HashMap<>(keys);
        }
        
        public int getWidth() {
            return pattern[0].length();
        }
        
        public int getHeight() {
            return pattern.length;
        }
        
        public NonNullList<RecipeItemStack> getIngredients() {
            return Arrays.stream(pattern).flatMap(s -> s.chars().boxed())
                    .map(i -> (char) i.intValue())
                    .map(keys::get)
                    .collect(Collectors.toCollection(NonNullList::a));
        }
        
        @Override
        public String toString() {
            return "Mojang Shape {"
                    + Arrays.toString(pattern) + "," 
                    + Objects.toString(keys)
                    + "}";
        }
    }
    
    @Override
    public ShapedRecipe toBukkitRecipe() {
        ShapedRecipe bukkit = new ShapedRecipe(CraftNamespacedKey.fromMinecraft(this.key), CraftItemStack.asCraftMirror(b()));
        Shape shape = getShape();
        
        bukkit.shape(shape.pattern);
        shape.getKeys().forEach((key, ingredient) -> setIngredient(bukkit, key, ingredient));
                
        return bukkit;
    }
    
    @SuppressWarnings("deprecation")
    private void setIngredient(ShapedRecipe bukkit, char c, RecipeItemStack ingredient) {
        if (ingredient == RecipeItemStack.a) {
            bukkit.setIngredient(c, new MaterialData(Material.AIR));
            return;
        }
        
        ItemStack[] choices = ingredient.choices;
        if (choices == null || choices.length == 0) {
            bukkit.setIngredient(c, new MaterialData(Material.AIR));
            return;
        }
        
        if (choices.length == 1) {
            MaterialData data = CraftItemStack.asCraftMirror(choices[0]).getData();
            bukkit.setIngredient(c, data);
        }
        
        bukkit.setIngredient(c, CraftItemStack.asCraftMirror(choices[0]).getType(), Short.MAX_VALUE); //best effort
    }
    
    @Override
    public String toString() {
        return "Mojang Shaped Recipe {"
                + "result = " + b()
                + "shape = " + getShape()
                + "group = " + getGroup()
                + "}";
    }

}
