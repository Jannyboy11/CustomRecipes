package com.gmail.jannyboy11.customrecipes.impl.furnace;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.Bukkit2NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class CRFurnaceRecipe implements FurnaceRecipe, NBTSerializable {

    private final NMSFurnaceRecipe nmsRecipe;
    
    public CRFurnaceRecipe(NMSFurnaceRecipe injectedFurnaceRecipe) {
        this.nmsRecipe = injectedFurnaceRecipe;
    }
    
    public CRFurnaceRecipe(NBTTagCompound serialized) {
        this(new NMSFurnaceRecipe(serialized));
    }

    
    public static NMSFurnaceRecipe getNMSRecipe(FurnaceRecipe bukkit) {
        if (bukkit instanceof CRFurnaceRecipe) {
            return ((CRFurnaceRecipe) bukkit).nmsRecipe;
        } else {
            return new Bukkit2NMSFurnaceRecipe(bukkit);
        }
    }
    
    public NMSFurnaceRecipe getHandle() {
        return nmsRecipe;
    }
    
    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(nmsRecipe.getKey());
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return nmsRecipe.serializeToNbt();
    }

    @Override
    public CraftingIngredient getIngredient() {
        return CRCraftingIngredient.asBukkitIngredient(nmsRecipe.getIngredient());
    }

    @Override
    public CraftItemStack getResult() {
        return CraftItemStack.asCraftMirror(nmsRecipe.getResult());
    }

    @Override
    public float getXp() {
        return nmsRecipe.getExperience();
    }

    @Override
    public void setIngredient(CraftingIngredient ingredient) {
        nmsRecipe.setIngredient(CRCraftingIngredient.asNMSIngredient(ingredient));
    }

    @Override
    public void setResult(org.bukkit.inventory.ItemStack result) {
        nmsRecipe.setResult(CraftItemStack.asNMSCopy(result));
    }

    @Override
    public void setXp(float xp) {
        nmsRecipe.setExperience(xp);
    }
}
