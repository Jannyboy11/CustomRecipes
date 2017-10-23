package com.gmail.jannyboy11.customrecipes.impl.modify.nms;

import java.util.Objects;

import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.CraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.MinecraftKey;

public class Bukkit2NMSExtendedCraftingIngredientModifier implements ExtendedCraftingIngredientModifier<ExtendedCraftingIngredient, ExtendedCraftingIngredient> {

    private final CraftingIngredientModifier bukkit;
    
    public Bukkit2NMSExtendedCraftingIngredientModifier(CraftingIngredientModifier bukkit) {
        this.bukkit = Objects.requireNonNull(bukkit);
    }
    
    @Override
    public ExtendedCraftingIngredient modify(ExtendedCraftingIngredient base) {
        CraftingIngredient bukkitIngredient = RecipeUtils.getBukkitCraftingIngredient(base);
        CraftingIngredient bukkitModifiedIngredient = bukkit.modify(bukkitIngredient);
        return RecipeUtils.getNMSCraftingIngredient(bukkitModifiedIngredient);
    }

    public CraftingIngredientModifier<?, ?> getBukkitModifier() {
        return bukkit;
    }

    @Override
    public MinecraftKey getKey() {
       return CraftNamespacedKey.toMinecraft(bukkit.getKey());
    }
}
