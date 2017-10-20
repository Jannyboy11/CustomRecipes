package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.server.v1_12_R1.ItemStack;

public interface ExtendedCraftingIngredient extends Predicate<ItemStack> {
    
    public default ItemStack getRemainder(ItemStack ingredient) {
        if (ingredient == null) return ItemStack.a;
        
        return ingredient.getItem().r() /*hasCraftingRemainder*/
                ? new ItemStack(ingredient.getItem().q() /*getCraftingRemainder*/)
                : ingredient;
    }
    
    public boolean accepts(ItemStack input);
    
    @Override
    public default boolean test(ItemStack toTest) {
        return accepts(toTest);
    }

    public default Optional<ItemStack> firstItemStack() {
        return Optional.empty();
    }

}
