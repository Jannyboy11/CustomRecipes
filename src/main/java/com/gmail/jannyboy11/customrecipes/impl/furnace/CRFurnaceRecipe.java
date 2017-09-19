package com.gmail.jannyboy11.customrecipes.impl.furnace;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.Bukkit2NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;

public class CRFurnaceRecipe<N extends NMSFurnaceRecipe> implements FurnaceRecipe {

    protected final N nmsRecipe;
    
    public CRFurnaceRecipe(N nmsFurnaceRecipe) {
        this.nmsRecipe = nmsFurnaceRecipe;
    }
    
    //TODO move this to RecipeUtils
    @Deprecated
    public static NMSFurnaceRecipe getNMSRecipe(FurnaceRecipe bukkit) {
        if (bukkit instanceof CRFurnaceRecipe) {
            return ((CRFurnaceRecipe) bukkit).nmsRecipe;
        } else {
            return new Bukkit2NMSFurnaceRecipe(bukkit);
        }
    }
    
    public N getHandle() {
        return nmsRecipe;
    }
    
    @Override
    public NamespacedKey getKey() {
        return CraftNamespacedKey.fromMinecraft(nmsRecipe.getKey());
    }

    @Override
    public boolean isIngredient(ItemStack input) {
        return nmsRecipe.checkInput(CraftItemStack.asNMSCopy(input));
    }

    @Override
    public CraftItemStack smelt(ItemStack input) {
        return CraftItemStack.asCraftMirror(nmsRecipe.getResult(CraftItemStack.asNMSCopy(input)));
    }

    @Override
    public float experienceReward(ItemStack input) {
        return nmsRecipe.getExperience(CraftItemStack.asNMSCopy(input));
    }
    
}
