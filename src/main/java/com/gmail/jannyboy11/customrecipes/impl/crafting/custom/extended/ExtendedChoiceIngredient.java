package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import java.util.List;
import java.util.Optional;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;

public interface ExtendedChoiceIngredient extends ExtendedCraftingIngredient {
    
    public NonNullList<ItemStack> getChoices();
    
    @Override
    public default Optional<ItemStack> firstItemStack() {
        List<ItemStack> choices = getChoices();
        return choices.isEmpty() ? Optional.empty() : Optional.of(choices.get(0));
    }

    @Override
    public default boolean accepts(ItemStack input) {
        if (input == null) return false;
        
        NonNullList<ItemStack> choices = getChoices();
        return choices.stream()
                .filter(choice -> choice.getItem() == input.getItem())
                .anyMatch(choice -> {
                   int data = choice.getData();
                   return data == Short.MAX_VALUE || data == input.getData();
                });
    }
    
}
