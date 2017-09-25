package com.gmail.jannyboy11.customrecipes.impl.furnace.custom;

import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.FixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.NMSFixedFurnaceRecipe;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

public class Bukkit2NMSFixedFurnaceRecipe extends NMSFixedFurnaceRecipe {

    private final FixedFurnaceRecipe bukkit;

    public Bukkit2NMSFixedFurnaceRecipe(FixedFurnaceRecipe bukkit) {
        super(CraftNamespacedKey.toMinecraft(bukkit.getKey()),
                CraftItemStack.asNMSCopy(bukkit.getIngredient()),
                CraftItemStack.asNMSCopy(bukkit.getResult()),
                bukkit.getExperience());
        this.bukkit = bukkit;
    }

    @Override
    public ItemStack getIngredient() {
        return CraftItemStack.asNMSCopy(bukkit.getIngredient());
    }

    @Override
    public ItemStack getResult() {
        return CraftItemStack.asNMSCopy(bukkit.getResult());
    }

    @Override
    public float getExperience() {
        return bukkit.getExperience();
    }

    @Override
    public boolean checkInput(ItemStack input) {
        return bukkit.isIngredient(CraftItemStack.asCraftMirror(input));
    }

    @Override
    public ItemStack getResult(ItemStack input) {
        return CraftItemStack.asNMSCopy(bukkit.smelt(CraftItemStack.asCraftMirror(input)));
    }

    @Override
    public float getExperience(ItemStack input) {
        return bukkit.experienceReward(CraftItemStack.asCraftMirror(input));
    }

    @Override
    protected FixedFurnaceRecipe createBukkitRecipe() {
        return bukkit;
    }

    @Override
    public FixedFurnaceRecipe getBukkitRecipe() {
        return bukkit;
    }
}
