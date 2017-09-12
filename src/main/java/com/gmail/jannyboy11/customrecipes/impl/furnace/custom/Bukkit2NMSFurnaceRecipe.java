package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import java.util.Objects;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class Bukkit2NMSFurnaceRecipe extends NMSFurnaceRecipe {
    
    public Bukkit2NMSFurnaceRecipe(FurnaceRecipe bukkit) {
        this.bukkit = Objects.requireNonNull(bukkit);
    }
    
    @Override
    public MinecraftKey getKey() {
        return CraftNamespacedKey.toMinecraft(bukkit.getKey());
    }
    
    @Override
    public RecipeItemStack getIngredient() {
        return CRCraftingIngredient.asNMSIngredient(bukkit.getIngredient());
    }
    
    @Override
    public ItemStack getResult() {
        return CraftItemStack.asNMSCopy(bukkit.getResult());
    }
    
    @Override
    public float getExperience() {
        return bukkit.getXp();
    }
    
    @Override
    public Bukkit2NMSFurnaceRecipe setIngredient(RecipeItemStack ingredient) {
        bukkit.setIngredient(CRCraftingIngredient.asBukkitIngredient(ingredient));
        return this;
    }
    
    public Bukkit2NMSFurnaceRecipe setResult(ItemStack result) {
        bukkit.setResult(CraftItemStack.asCraftMirror(result));
        return this;
    }
    
    public Bukkit2NMSFurnaceRecipe setExperience(float experience) {
        bukkit.setXp(experience);
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Bukkit2NMSFurnaceRecipe)) return false;
        
        Bukkit2NMSFurnaceRecipe that = (Bukkit2NMSFurnaceRecipe) o;
        return Objects.equals(this.bukkit, that.bukkit);
    }
    
    @Override
    public int hashCode() {
        return bukkit.hashCode();
    }

}
