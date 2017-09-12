package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class NMSFurnaceRecipe implements NBTSerializable {
    private final MinecraftKey key;
    private RecipeItemStack ingredient;
    private ItemStack result;
    private float experience;
    
    protected FurnaceRecipe bukkit;
    
    protected NMSFurnaceRecipe() {
        this.key = null;
    }
    
    public NMSFurnaceRecipe(MinecraftKey key, ItemStack ingredient, ItemStack result, float experience) {
        this(key, RecipeItemStack.a(new ItemStack[] {ingredient}), result, experience);
    }
    
    public NMSFurnaceRecipe(MinecraftKey key, RecipeItemStack ingredient, ItemStack result, float experience) {
        this.key = key;
        this.ingredient = ingredient;
        this.result = result;
        this.experience = experience;
    }
    
    public NMSFurnaceRecipe(NBTTagCompound serialized) {
        this.key = NBTUtil.deserializeKey(serialized.getCompound("key"));
        this.ingredient = NBTUtil.deserializeRecipeItemStack(serialized.getCompound("ingredient"));
        this.result = NBTUtil.deserializeItemStack(serialized.getCompound("result"));
        this.experience = serialized.getFloat("experience");
    }
    
    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.set("ingredient", NBTUtil.serializeRecipeItemStack(getIngredient()));
        compound.set("result", NBTUtil.serializeItemStack(getResult()));
        compound.setFloat("experience", getExperience());
        compound.set("key", NBTUtil.serializeKey(getKey()));
        return compound;
    }
    
    public MinecraftKey getKey() {
        return key;
    }
    
    public RecipeItemStack getIngredient() {
        return ingredient;
    }
    
    public ItemStack getResult() {
        return result;
    }
    
    public float getExperience() {
        return experience;
    }
    
    public NMSFurnaceRecipe setIngredient(RecipeItemStack ingredient) {
        this.ingredient = ingredient;
        return this;
    }
    
    public NMSFurnaceRecipe setResult(ItemStack result) {
        this.result = result;
        return this;
    }
    
    public NMSFurnaceRecipe setExperience(float experience) {
        this.experience = experience;
        return this;
    }
    
    public FurnaceRecipe getBukkitRecipe() {
        return bukkit == null ? bukkit = new CRFurnaceRecipe(this) : bukkit;
    }
    
    @Override
    public boolean equals(Object o) {
        //TODO can we do better?
        if (o == this) return true;
        return (o instanceof NMSFurnaceRecipe) && ((Objects.equals(this.key, ((NMSFurnaceRecipe) o).getKey())));
    }
    
    @Override
    public int hashCode() {
        //TODO can we do better?
        return Objects.hashCode(key);
    }
    
}
