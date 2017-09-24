package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.NMSFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;
import com.google.common.collect.Iterators;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.RecipesFurnace;

public class NMSFurnaceManager extends RecipesFurnace implements Iterable<NMSFurnaceRecipe> {
    
    private final Map<MinecraftKey, NMSFurnaceRecipe> vanillaRecipes;
    private final Map<MinecraftKey, NMSFurnaceRecipe> customRecipes;
    
    private static NMSFurnaceManager instance;
    
    protected NMSFurnaceManager() {
        super();
        
        this.vanillaRecipes = new HashMap<>();
        this.customRecipes = new HashMap<>();
    
        setInstance(this);        
        
        copyVanillaRecipes();
        copyCustomRecipes();
    }
    
    public static NMSFurnaceManager getInstance() {
        return instance == null ? instance = new NMSFurnaceManager() : instance;
    }
    
    protected static void setInstance(NMSFurnaceManager recipesFurnace) {
        ReflectionUtil.setStaticFinalFieldValue(RecipesFurnace.class, "a", recipesFurnace);
        instance = recipesFurnace;
    }
    
    
    private void copyVanillaRecipes() {
        super.recipes.forEach((ingredient, result) -> {
            Item item = ingredient.getItem();
            MinecraftKey key = new MinecraftKey(item.getName());
            float xp = super.b(ingredient);
            
            NMSFurnaceRecipe injectedFurnaceRecipe = new NMSFixedFurnaceRecipe(key, ingredient, result, xp);
            addVanillaRecipe(injectedFurnaceRecipe);
        });
    }
    
    private void copyCustomRecipes() {
        super.customRecipes.forEach((ingredient, result) -> {
            Item item = ingredient.getItem();
            MinecraftKey key = CraftNamespacedKey.toMinecraft(CustomRecipesPlugin.getInstance().getKey(item.getName()));
            float xp = super.b(ingredient);
            
            NMSFurnaceRecipe injectedFurnaceRecipe = new NMSFixedFurnaceRecipe(key, ingredient, result, xp);
            addCustomRecipe(injectedFurnaceRecipe);
        });
    }

    protected final RecipeItemStack makeVanillaIngredient(ItemStack referenceStack) {
        return new InjectedIngredient(itemStack -> furnaceEqualsVanilla(this, itemStack, referenceStack)).asNMSIngredient();
    }
    
    public static boolean furnaceEqualsVanilla(RecipesFurnace recipesFurnace, ItemStack test, ItemStack referenceStack) {
        return (boolean) ReflectionUtil.invokeInstanceMethod(recipesFurnace, "a", test, referenceStack);
    }

    @Override
    public Iterator<NMSFurnaceRecipe> iterator() {
        return Iterators.concat(customRecipes.values().iterator(), vanillaRecipes.values().iterator());
    }
    
    @Override
    public void a(ItemStack ingredient, ItemStack result, float xp) {
        super.a(ingredient, result, xp);
        if (instance == null) return; //if we are called from the super constructor, return. ugly hack!        
        
        MinecraftKey key = new MinecraftKey(ingredient.getItem().getName());
        NMSFurnaceRecipe recipe = new NMSFixedFurnaceRecipe(key, ingredient, result, xp);

        addVanillaRecipe(recipe);
    }
    
    @Override
    public void registerRecipe(ItemStack ingredient, ItemStack result, float xp) {
        super.a(ingredient, result, xp);
        if (instance == null) return; //if we are called from the super constructor, return. ugly hack!
        
        MinecraftKey key = CraftNamespacedKey.toMinecraft(CustomRecipesPlugin.getInstance().getKey(ingredient.getItem().getName()));
        NMSFurnaceRecipe recipe = new NMSFixedFurnaceRecipe(key, ingredient, result, xp);

        addCustomRecipe(recipe);
    }
    
    @Override
    public ItemStack getResult(ItemStack input) {
        for (NMSFurnaceRecipe recipe : this) {
           if (recipe.checkInput(input)) {
               return recipe.getResult(input);
           }
        }
        return ItemStack.a;
    }
    
    @Override
    public float b(ItemStack input) {
        for (NMSFurnaceRecipe recipe : this) {
            if (recipe.checkInput(input)) {
                return recipe.getExperience(input);
            }
        }
        return 0F;
    }
    
    
    public void clear() {
        clearVanilla();
        clearCustom();
    }
    
    public void clearVanilla() {
        super.recipes = new HashMap<>();
        
        vanillaRecipes.clear();
    }
    
    public void clearCustom() {
        super.customRecipes.clear();
        super.customExperience.clear();
        
        customRecipes.clear();
    }
    
    public void reset() {
        clear();
        copyVanillaRecipes();
    }
    
    public NMSFurnaceRecipe getRecipe(MinecraftKey key) {
        NMSFurnaceRecipe recipe = getCustomRecipe(key);
        if (recipe == null) recipe = getVanillaRecipe(key);
        return recipe;
    }
    
    public NMSFurnaceRecipe getVanillaRecipe(MinecraftKey key) {
        return vanillaRecipes.get(key);
    }
    
    public NMSFurnaceRecipe getCustomRecipe(MinecraftKey key) {
        return customRecipes.get(key);
    }
    
    public void addVanillaRecipe(NMSFurnaceRecipe nms) {
        vanillaRecipes.put(nms.getKey(), nms);
    }
    
    public void addCustomRecipe(NMSFurnaceRecipe nms) {
        customRecipes.put(nms.getKey(), nms);
    }
    
    public Iterator<NMSFurnaceRecipe> vanillaIterator() {
        return vanillaRecipes.values().iterator();
    }
    
    public Iterator<NMSFurnaceRecipe> customIterator() {
        return customRecipes.values().iterator();
    }
    
    protected static NMSFurnaceRecipe getRecipe(ItemStack ingredientTest, Iterator<? extends NMSFurnaceRecipe> iterator) {
        while (iterator.hasNext()) {
            NMSFurnaceRecipe recipe = iterator.next();
            if (recipe.checkInput(ingredientTest)) {
                return recipe;
            }
        }
        return null;
    }
    
    public NMSFurnaceRecipe getRecipe(ItemStack itemStack) {
        return getRecipe(itemStack, iterator());
    }
    
    public NMSFurnaceRecipe getCustomRecipe(ItemStack itemStack) {
        return getRecipe(itemStack, customIterator());
    }
    
    public NMSFurnaceRecipe getVanillaRecipe(ItemStack itemStack) {
        return getRecipe(itemStack, vanillaIterator());
    }

    protected static NMSFurnaceRecipe removeRecipe(ItemStack ingredientTest, Iterator<? extends NMSFurnaceRecipe> iterator) {
        while (iterator.hasNext()) {
            NMSFurnaceRecipe recipe = iterator.next();
            if (recipe.checkInput(ingredientTest)) {
                iterator.remove();
                return recipe;
            }
        }
        return null;
    }
    
    public NMSFurnaceRecipe removeRecipe(ItemStack ingredient) {
        return removeRecipe(ingredient, iterator());
    }

    public NMSFurnaceRecipe removeVanillaRecipe(ItemStack ingredient) {
        return removeRecipe(ingredient, vanillaIterator());
    }
    
    public NMSFurnaceRecipe removeCustomRecipe(ItemStack ingredient) {
        return removeRecipe(ingredient, customIterator());
    }
    
    public NMSFurnaceRecipe removeVanillaRecipe(MinecraftKey key) {
        return vanillaRecipes.remove(key);
    }
    
    public NMSFurnaceRecipe removeCustomRecipe(MinecraftKey key) {
        return customRecipes.remove(key);
    }
    
    public NMSFurnaceRecipe removeRecipe(MinecraftKey key) {
        NMSFurnaceRecipe removedRecipe = removeCustomRecipe(key);
        return removedRecipe != null ? removedRecipe : removeVanillaRecipe(key);
    }
    
}
