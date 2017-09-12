package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;
import com.google.common.collect.Iterators;

import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.RecipesFurnace;

//TODO use InventoryUtils.getItemName for the NamespacedKeys?
public class NMSFurnaceManager extends RecipesFurnace implements Iterable<NMSFurnaceRecipe> {
    
    private final Map<MinecraftKey, NMSFurnaceRecipe> vanillaRecipes;
    private final Map<MinecraftKey, NMSFurnaceRecipe> customRecipes;
    
    private static NMSFurnaceManager instance;
    
    protected NMSFurnaceManager() {
        super();
        
        this.vanillaRecipes = new HashMap<>();
        this.customRecipes = new HashMap<>();
    
        setInstance(this);        
        
        copyVanillaRecipes(this);
        copyCustomRecipes(this);
    }
    
    public static NMSFurnaceManager getInstance() {
        return instance == null ? instance = new NMSFurnaceManager() : instance;
    }
    
    protected static void setInstance(NMSFurnaceManager recipesFurnace) {
        ReflectionUtil.setStaticFinalFieldValue(RecipesFurnace.class, "a", recipesFurnace);
        instance = recipesFurnace;
    }
    
    
    private void copyVanillaRecipes(RecipesFurnace instance) {
        instance.recipes.forEach((ingredient, result) -> {                
            Item item = ingredient.getItem();
            MinecraftKey key = new MinecraftKey(item.getName());
            RecipeItemStack newIngredient = makeVanillaIngredient(ingredient);
            float xp = super.b(ingredient);
            
            NMSFurnaceRecipe injectedFurnaceRecipe = new NMSFurnaceRecipe(key, newIngredient, result, xp);
            vanillaRecipes.put(key, injectedFurnaceRecipe);
        });
    }
    
    private void copyCustomRecipes(RecipesFurnace instance) {
        instance.customRecipes.forEach((ingredient, result) -> {
            Item item = ingredient.getItem();
            MinecraftKey key = CraftNamespacedKey.toMinecraft(CustomRecipesPlugin.getInstance().getKey(item.getName()));
            RecipeItemStack newIngredient = makeVanillaIngredient(ingredient);
            float xp = super.b(ingredient);
            
            NMSFurnaceRecipe injectedFurnaceRecipe = new NMSFurnaceRecipe(key, newIngredient, result, xp);
            customRecipes.put(key, injectedFurnaceRecipe);
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
    public void a(ItemStack input, ItemStack result, float xp) {
        super.a(input, result, xp);
        if (instance == null) return; //if we are called from the super constructor, return. ugly hack!        
        
        MinecraftKey key = new MinecraftKey(input.getItem().getName());
        RecipeItemStack ingredient = makeVanillaIngredient(input);
        NMSFurnaceRecipe recipe = new NMSFurnaceRecipe(key, ingredient, result, xp);
        vanillaRecipes.put(key, recipe);
    }
    
    @Override
    public void registerRecipe(ItemStack input, ItemStack result, float xp) {
        super.a(input, result, xp);
        if (instance == null) return; //if we are called from the super constructor, return. ugly hack!
        
        MinecraftKey key = CraftNamespacedKey.toMinecraft(CustomRecipesPlugin.getInstance().getKey(input.getItem().getName()));
        RecipeItemStack ingredient = makeVanillaIngredient(input);
        NMSFurnaceRecipe recipe = new NMSFurnaceRecipe(key, ingredient, result, xp);
        customRecipes.put(key, recipe);
    }
    
    @Override
    public ItemStack getResult(ItemStack itemstack) {
        for (NMSFurnaceRecipe recipe : this) {
           if (recipe.getIngredient().a(itemstack)) {
               return recipe.getResult();
           }
        }
        return ItemStack.a;
    }
    
    @Override
    public float b(ItemStack itemstack) {
        for (NMSFurnaceRecipe recipe : this) {
            if (recipe.getIngredient().a(itemstack)) {
                return recipe.getExperience();
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
        copyVanillaRecipes(new RecipesFurnace());
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
            if (recipe.getIngredient().a(ingredientTest)) {
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

    protected static NMSFurnaceRecipe removeRecipe(ItemStack ingredient, Iterator<? extends NMSFurnaceRecipe> iterator) {
        while (iterator.hasNext()) {
            NMSFurnaceRecipe recipe = iterator.next();
            if (recipe.getIngredient().a(ingredient)) {
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
