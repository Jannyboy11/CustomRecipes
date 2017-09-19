package com.gmail.jannyboy11.customrecipes.impl.furnace;

import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.FixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.NMSFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class CRFixedFurnaceRecipe extends CRFurnaceRecipe<NMSFixedFurnaceRecipe> implements FixedFurnaceRecipe, NBTSerializable {

    public CRFixedFurnaceRecipe(NMSFixedFurnaceRecipe injectedFurnaceRecipe) {
        super(injectedFurnaceRecipe);
    }
    
    public CRFixedFurnaceRecipe(NBTTagCompound serialized) {
        this(new NMSFixedFurnaceRecipe(serialized));
    }

    @Override
    public CraftItemStack getResult() {
        return CraftItemStack.asCraftMirror(nmsRecipe.getResult());
    }

    @Override
    public CraftItemStack getIngredient() {
        return CraftItemStack.asCraftMirror(nmsRecipe.getIngredient());
    }

    @Override
    public float getExperience() {
        return nmsRecipe.getExperience();
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return nmsRecipe.serializeToNbt();
    }

    @Override
    public Map<String, Object> serialize() {
        return NBTSerializable.super.serialize();
    }

}
