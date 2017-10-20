package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.MatchStrategy;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.World;

//TODO why did I create this class again?
//TODO I think it was to create some common superclass for shaped and shapeless recipes
//TODO and to allow other plugins to register their own 'base' type (matching strategy) i think it was.
public class ExtendedMatchRecipe implements ExtendedCraftingRecipe {
    
    private static final Map<MatchStrategy, Function<? extends ExtendedCraftingRecipe, ? extends CraftingRecipe>> STRATEGY_MAPPER = new HashMap<>();
    static {
        STRATEGY_MAPPER.put(VanillaMatchStrategy.SHAPED, null); //TODO
        STRATEGY_MAPPER.put(VanillaMatchStrategy.SHAPELESS, null); //TODO
    }
    
    private final MinecraftKey key;
    private final ItemStack result;
    private final NonNullList<? extends ExtendedCraftingIngredient> ingredients;
    private final MatchStrategy matcher;
    private final String group;
    
    public ExtendedMatchRecipe(MinecraftKey key, ItemStack result, NonNullList<? extends ExtendedCraftingIngredient> ingredients, MatchStrategy strat, String group) {
        this.key = key;
        this.result = result;
        this.ingredients = ingredients;
        this.matcher = strat;
        this.group = group;
    }

    @Override
    public ItemStack craftItem(InventoryCrafting inventory) {
        return result.cloneItemStack();
    }

    @Override
    public void setKey(MinecraftKey key) {
    }

    @Override
    public Recipe toBukkitRecipe() {
        if (matcher == VanillaMatchStrategy.SHAPED) {
            //TODO return shaped bukkit recipe
        }
        
        if (matcher == VanillaMatchStrategy.SHAPELESS) {
            //TODO return shapeless bukkit recipe
        }
        
        //fallback on CustomRecipes version
        return getBukkitRecipe();
    }

    @Override
    public MinecraftKey getKey() {
        return key;
    }

    @Override
    public CraftingRecipe getBukkitRecipe() {
        // TODO switch on match strategy, and return a CRShaped, CRShapeless or CRCraftingRecipe
        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        return matcher.matches(this, inventory, world);
    }

    @Override
    public ItemStack getResult() {
        return result;
    }
    
    @Override
    public NonNullList<? extends ExtendedCraftingIngredient> getIngredients() {
        return ingredients;
    }

    @Override
    public NonNullList<ItemStack> getRemainders(InventoryCrafting inventory) {
        NonNullList<ItemStack> remainders = NonNullList.a(inventory.getSize(), ItemStack.a);
        ListIterator<ItemStack> remainderIterator = remainders.listIterator();
        
        List<ItemStack> matrix = inventory.getContents();
        NonNullList<? extends ExtendedCraftingIngredient> ingredients = getIngredients();
        
        Iterator<ItemStack> matrixIterator = matrix.iterator();
        Iterator<? extends ExtendedCraftingIngredient> ingredientIterator = ingredients.iterator();
        
        while (matrixIterator.hasNext() && ingredientIterator.hasNext() && remainderIterator.hasNext()) {
            ItemStack content = matrixIterator.next();
            ExtendedCraftingIngredient ingredient = ingredientIterator.next();
            
            ItemStack remainder = ingredient.getRemainder(content);

            remainderIterator.next();
            remainderIterator.set(remainder);
        }
        
        return remainders;
    }

    @Override
    public boolean isHidden() {
        return false;
    }
    
    @Override
    public String getGroup() {
        return group;
    }

}
