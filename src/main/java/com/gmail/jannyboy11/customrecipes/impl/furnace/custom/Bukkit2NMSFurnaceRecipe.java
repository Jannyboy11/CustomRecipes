package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import java.util.Objects;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class Bukkit2NMSFurnaceRecipe extends NMSFurnaceRecipe {
    
    public Bukkit2NMSFurnaceRecipe(FurnaceRecipe bukkit) {
        super(CraftNamespacedKey.toMinecraft(bukkit.getKey()));
        this.bukkit = Objects.requireNonNull(bukkit);
    }
    
    @Override
    public MinecraftKey getKey() {
        return CraftNamespacedKey.toMinecraft(bukkit.getKey());
    }
    
    public boolean checkInput(ItemStack input) {
        return bukkit.isIngredient(CraftItemStack.asCraftMirror(input));
    }
    
    public ItemStack getResult(ItemStack input) {
        return CraftItemStack.asNMSCopy(bukkit.smelt(CraftItemStack.asCraftMirror(input)));
    }
    
    public float getExperience(ItemStack input) {
        return bukkit.experienceReward(CraftItemStack.asCraftMirror(input));
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
    
    @Override
    protected FurnaceRecipe createBukkitRecipe() {
        return bukkit;
    }
    
    @Override
    public FurnaceRecipe getBukkitRecipe() {
        return bukkit;
    }

}
