package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.Optional;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;

//TODO extends CRIngredient?
public class CRCraftingIngredient implements CraftingIngredient {
    
    private final ExtendedCraftingIngredient handle;
    
    public CRCraftingIngredient(ExtendedCraftingIngredient handle) {
        this.handle = handle;
    }

    @Override
    public boolean isIngredient(org.bukkit.inventory.ItemStack input) {
        ItemStack nmsInput = CraftItemStack.asNMSCopy(input);
        return handle.accepts(nmsInput);
    }
    
    @Override
    public Optional<CraftItemStack> firstItemStack() {
        return handle.firstItemStack().map(CraftItemStack::asCraftMirror);
    }
    
    public CraftItemStack getRemainder(org.bukkit.inventory.ItemStack input) {
        ItemStack nmsInput = CraftItemStack.asNMSCopy(input);
        ItemStack nmsRemainder = handle.getRemainder(nmsInput);
        return CraftItemStack.asCraftMirror(nmsRemainder);
    }
    
    public ExtendedCraftingIngredient getHandle() {
        return handle;
    }

}
