package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.Objects;
import java.util.Optional;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;

public class Bukkit2NMSCraftingIngredient implements ExtendedCraftingIngredient {
    
    private final CraftingIngredient bukkit;

    public Bukkit2NMSCraftingIngredient(CraftingIngredient bukkit) {
        this.bukkit = Objects.requireNonNull(bukkit);
    }

    @Override
    public boolean accepts(ItemStack input) {
        CraftItemStack bukkitInput = CraftItemStack.asCraftMirror(input);
        return bukkit.isIngredient(bukkitInput);
    }
    
    @Override
    public ItemStack getRemainder(ItemStack ingredient) {
        CraftItemStack bukkitIngredient = CraftItemStack.asCraftMirror(ingredient);
        org.bukkit.inventory.ItemStack bukkitRemainder = bukkit.getRemainder(bukkitIngredient);
        return CraftItemStack.asNMSCopy(bukkitRemainder);
    }
    
    @Override
    public Optional<ItemStack> firstItemStack() {
        return bukkit.firstItemStack().map(CraftItemStack::asNMSCopy);
    }
    
    public CraftingIngredient getBukkitIngredient() {
        return bukkit;
    }

}
