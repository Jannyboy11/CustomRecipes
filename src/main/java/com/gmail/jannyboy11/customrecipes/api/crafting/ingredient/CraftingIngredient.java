package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient;

import java.lang.reflect.Proxy;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.CraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.ModifiedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.api.modify.ModifiedInvocationHandler;
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
    
    @SuppressWarnings("rawtypes")
    public default Class<? extends ModifiedCraftingIngredient> getModifiedType() {
        return ModifiedCraftingIngredient.class;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public default <R extends CraftingIngredient> ModifiedCraftingIngredient<? extends CraftingIngredient> applyModifier(CraftingIngredientModifier<? extends CraftingIngredient, R> modifier) {
        ModifiedInvocationHandler handler = new ModifiedInvocationHandler(this, modifier);
        ModifiedCraftingIngredient proxy = (ModifiedCraftingIngredient) Proxy.newProxyInstance(ModifiedCraftingIngredient.class.getClassLoader(),
                new Class[] {getModifiedType()}, handler);
        
        return proxy;
    }

}
