package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.stream.Collectors;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedChoiceIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;

public class Bukkit2NMSChoiceIngredient extends Bukkit2NMSCraftingIngredient implements ExtendedChoiceIngredient {

    public Bukkit2NMSChoiceIngredient(ChoiceIngredient bukkit) {
        super(bukkit);
    }
    
    @Override
    public ChoiceIngredient getBukkitIngredient() {
        return (ChoiceIngredient) super.getBukkitIngredient();
    }

    @Override
    public NonNullList<ItemStack> getChoices() {
        return getBukkitIngredient().getChoices().stream()
                .map(CraftItemStack::asNMSCopy)
                .collect(Collectors.toCollection(NonNullList::a));
    }

}
