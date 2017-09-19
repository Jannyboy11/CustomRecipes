package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSFurnaceRecipe {
    
    private final MinecraftKey key;
    
    private Predicate<? super ItemStack> ingredient;
    private Function<? super ItemStack, ? extends ItemStack> result;
    private Function<? super ItemStack, ? extends Float> experience;
    
    protected FurnaceRecipe bukkit;
    
    protected NMSFurnaceRecipe(MinecraftKey key) {
        this.key = Objects.requireNonNull(key);
    }
    
    public NMSFurnaceRecipe(MinecraftKey key, Predicate<? super ItemStack> ingredient,
            Function<? super ItemStack, ? extends ItemStack> result,
            Function<? super ItemStack, ? extends Float> experience) {
        this(key);
        this.ingredient = Objects.requireNonNull(ingredient);
        this.result = Objects.requireNonNull(result);
        this.experience = Objects.requireNonNull(experience);
    }
    
    public MinecraftKey getKey() {
        return key;
    }
    
    public boolean checkInput(ItemStack input) {
        return ingredient.test(input);
    }
    
    public ItemStack getResult(ItemStack input) {
        return result.apply(input);
    }
    
    public float getExperience(ItemStack input) {
        return experience.apply(input);
    }
    
    public FurnaceRecipe getBukkitRecipe() {
        return bukkit == null ? bukkit = createBukkitRecipe() : bukkit;
    }
    
    protected FurnaceRecipe createBukkitRecipe() {
        return new CRFurnaceRecipe<>(this);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return (o instanceof NMSFurnaceRecipe) && ((Objects.equals(this.key, ((NMSFurnaceRecipe) o).getKey())));
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }
    
    @Override
    public String toString() {
        return getClass().getName() +
                "{key()=" + getKey() +
                ",ingredient=" + ingredient +
                ",result=" + result +
                ",experience=" + experience +
                "}";
    }
    
}
