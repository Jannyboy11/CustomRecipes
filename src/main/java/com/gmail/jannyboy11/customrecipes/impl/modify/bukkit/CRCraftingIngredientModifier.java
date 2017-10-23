package com.gmail.jannyboy11.customrecipes.impl.modify.bukkit;

import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;

import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.modify.nms.ExtendedCraftingIngredientModifier;

public class CRCraftingIngredientModifier<NMS extends ExtendedCraftingIngredientModifier<ExtendedCraftingIngredient, ExtendedCraftingIngredient>>
        implements com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.CraftingIngredientModifier<CraftingIngredient, CraftingIngredient> {

    private final NMS nmsModifier;
    
    public CRCraftingIngredientModifier(NMS nms) {
        this.nmsModifier = Objects.requireNonNull(nms); 
    }

    @Override
    public CraftingIngredient modify(CraftingIngredient baseIngredient) {
        ExtendedCraftingIngredient nmsIngredient = RecipeUtils.getNMSCraftingIngredient(baseIngredient);
        ExtendedCraftingIngredient modified = nmsModifier.modify(nmsIngredient);
        return RecipeUtils.getBukkitCraftingIngredient(modified);
    }
    
    public NMS getHandle() {
        return nmsModifier;
    }

    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(nmsModifier.getKey());
    }
}
