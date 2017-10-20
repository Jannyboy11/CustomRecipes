package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient;

import java.util.List;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;

/**
 * The ingredient that only accepts the empty ItemStack.
 * <br>
 * The list of choices is always empty.
 * 
 * @author Jan
 *
 */
public interface EmptyIngredient extends ChoiceIngredient {
    
    @Override
    public default List<? extends ItemStack> getChoices() {
        return List.of();
    }
    
    @Override
    public default Optional<? extends ItemStack> firstItemStack() {
        return Optional.empty();
    }

}
