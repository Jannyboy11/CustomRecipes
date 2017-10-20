package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended;

import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl.RecipeItemStackAdapter;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.World;

public interface ExtendedCraftingRecipe extends IRecipe {
    
    public MinecraftKey getKey();
    
    public CraftingRecipe getBukkitRecipe();
    
    public default String getGroup() {
        return "";
    }
    
    public boolean matches(InventoryCrafting inventory, World world);
    
    @Override
    public default boolean a(InventoryCrafting inventory, World world) {
        return matches(inventory, world);
    }
    
    public ItemStack getResult();
    
    @Override
    public default ItemStack b() {
        return getResult();
    }
    
    public NonNullList<ItemStack> getRemainders(InventoryCrafting inventory);
    
    @Override
    public default NonNullList<ItemStack> b(InventoryCrafting inventory) {
        return getRemainders(inventory);
    }
    
    public default NonNullList<? extends ExtendedCraftingIngredient> getIngredients() {
        return this.d().stream()
                .map(RecipeItemStackAdapter::get)
                .collect(Collectors.toCollection(NonNullList::a));
    }
    
    public boolean isHidden();
    
    public default boolean c() {
        return isHidden();
    }

}
