package com.gmail.jannyboy11.customrecipes.api.crafting.ingredient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.ModifiedChoiceIngredient;

/**
 * A crafting ingredient that will accept items similar to items in the choices list.
 * <br>
 * The similarity check may differ per implementation.
 * 
 * @author Jan
 */
public interface ChoiceIngredient extends CraftingIngredient {

    /**
     * Get the list of choices.
     *
     * @return the list of choices
     */
    public List<? extends ItemStack> getChoices();

    /**
     * Serializes this ChoiceIngredient. The map contains at least the following:
     * <ul>
     *     <li>key: "choices", value: a List&lt? extends ItemStack&gt</li>
     * </ul>
     * <br>
     * Implementations may provide more entries in this map.
     *
     * @return the map.
     */
    public default Map<String, Object> serialize() {
        return Map.of("choices", getChoices());
    }
    
    
    @Override
    public default Optional<? extends ItemStack> firstItemStack() {
        return getChoices().stream().findFirst();
    }
    
    @SuppressWarnings("rawtypes")
    public default Class<? extends ModifiedChoiceIngredient> getModifiedType() {
        return ModifiedChoiceIngredient.class;
    }

}
