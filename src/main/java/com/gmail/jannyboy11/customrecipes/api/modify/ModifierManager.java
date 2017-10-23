package com.gmail.jannyboy11.customrecipes.api.modify;

import java.util.Iterator;

import org.bukkit.NamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.CraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;

public interface ModifierManager extends Iterable<IngredientModifier> {
    
    public boolean registerModifier(NamespacedKey key, IngredientModifier modifier);

    public void deregisterModifier(NamespacedKey key);
    
    public CraftingIngredientModifier<CraftingIngredient, CraftingIngredient> getNBTModifier();
    
    public CraftingIngredientModifier<CraftingIngredient, CraftingIngredient> getCountModifier();
    
    public Iterator<? extends CraftingIngredientModifier<?, ?>> craftingIngredientModifierIterator();
    
    public Iterator<? extends CraftingModifier<?, ?>> craftingRecipeModifierIterator();
    
}
