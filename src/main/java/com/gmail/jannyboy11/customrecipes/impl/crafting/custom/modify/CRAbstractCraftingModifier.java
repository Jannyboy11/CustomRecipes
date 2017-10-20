package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;

import java.util.Objects;

//eclipse you are drunk!
public class CRAbstractCraftingModifier<T extends CraftingRecipe, R extends CraftingRecipe, N extends NMSCraftingModifier> implements CraftingModifier<T, R> {

    protected final N nmsModifier;

    public CRAbstractCraftingModifier(N nmsModifier) {
        this.nmsModifier = Objects.requireNonNull(nmsModifier);
    }

    @Override
    public R modify(T base) {
        NMSCraftingRecipe nms = RecipeUtils.getNMSRecipe(base);
        IRecipe nmsModified = nmsModifier.modify(nms.getHandle());
        return (R) RecipeUtils.getBukkitRecipe(nmsModified);
    }

    public N getNmsModifier() {
        return nmsModifier;
    }
}
