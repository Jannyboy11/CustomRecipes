package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;

public interface CraftingIngredient extends Ingredient {
    
    public default ItemStack getRemainder(ItemStack ingredient) {
        ItemStack clone = ingredient == null ? new ItemStack(Material.AIR) : ingredient.clone();
        clone.setData(InventoryUtils.getIngredientRemainder(clone.getData()));
        return clone;
    }
    
    public default Optional<? extends ItemStack> firstItemStack() {
        return Optional.empty();
    }

}
