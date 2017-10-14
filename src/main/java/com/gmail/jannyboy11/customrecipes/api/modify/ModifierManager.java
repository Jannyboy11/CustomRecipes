package com.gmail.jannyboy11.customrecipes.api.modify;

import org.bukkit.NamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;

public interface ModifierManager extends Iterable<IngredientModifier> {
    
    public boolean registerModifier(NamespacedKey key, IngredientModifier modifier);

    public void deregisterModifier(NamespacedKey key);
    
}
