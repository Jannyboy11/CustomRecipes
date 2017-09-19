package com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.FixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class NMSFixedFurnaceRecipe extends NMSFurnaceRecipe implements NBTSerializable {

    private ItemStack ingredient;
    private ItemStack result;
    private float experience;
    
    public NMSFixedFurnaceRecipe(MinecraftKey key, ItemStack ingredient, ItemStack result, float experience) {
        super(key);
        this.ingredient = Objects.requireNonNull(ingredient);
        this.result = Objects.requireNonNull(result);
        this.experience = Objects.requireNonNull(experience);
    }
    
    public NMSFixedFurnaceRecipe(NBTTagCompound serialized) {
        super(NBTUtil.deserializeKey(serialized.getCompound("key")));
        this.ingredient = NBTUtil.deserializeItemStack(serialized.getCompound("ingredient"));
        this.result = NBTUtil.deserializeItemStack(serialized.getCompound("result"));
        this.experience = serialized.getFloat("experience");
    }
    
    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.set("ingredient", NBTUtil.serializeItemStack(getIngredient()));
        compound.set("result", NBTUtil.serializeItemStack(getResult()));
        compound.setFloat("experience", getExperience());
        compound.set("key", NBTUtil.serializeKey(getKey()));
        return compound;
    }
    
    @Override
    protected FixedFurnaceRecipe createBukkitRecipe() {
        return new CRFixedFurnaceRecipe(this);
    }
    
    public ItemStack getIngredient() {
        return ingredient;
    }
    
    public ItemStack getResult() {
        return result;
    }
    
    public float getExperience() {
        return experience;
    }
    
    @Override
    public boolean checkInput(ItemStack input) {
        NMSFurnaceManager instance = NMSFurnaceManager.getInstance();
        return NMSFurnaceManager.furnaceEqualsVanilla(instance, input, getIngredient());
    }
    
    @Override
    public ItemStack getResult(ItemStack input) {
        return getResult();
    }
    
    @Override
    public float getExperience(ItemStack input) {
        return getExperience();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof NMSFixedFurnaceRecipe)) return false;
        
        NMSFixedFurnaceRecipe that = (NMSFixedFurnaceRecipe) o;
        return Objects.equals(this.getKey(), that.getKey()) &&
                ItemStack.equals(this.getIngredient(), that.getIngredient()) &&
                ItemStack.equals(this.getResult(), that.getResult()) &&
                Float.floatToIntBits(this.getExperience()) == Float.floatToIntBits(that.getExperience());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getKey(), getIngredient(), getResult(), getExperience());
    }
    
    @Override
    public String toString() {
        return getClass().getName() +
                "{key()=" + getKey() +
                ",ingredient()=" + getIngredient() +
                ",result()=" + getResult() +
                ",experience()=" + getExperience() +
                "}";
    }
}
