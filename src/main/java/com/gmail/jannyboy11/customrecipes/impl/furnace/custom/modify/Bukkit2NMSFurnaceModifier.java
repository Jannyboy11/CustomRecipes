package com.gmail.jannyboy11.customrecipes.impl.furnace.custom.modify;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.furnace.modify.FurnaceModifier;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.Bukkit2NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;

public class Bukkit2NMSFurnaceModifier implements NMSFurnaceModifier<NMSFurnaceRecipe, NMSFurnaceRecipe> {
    
    private final FurnaceModifier bukkit;
    
    public Bukkit2NMSFurnaceModifier(FurnaceModifier bukkit) {
        this.bukkit = Objects.requireNonNull(bukkit);
    }

    @Override
    public NMSFurnaceRecipe modify(NMSFurnaceRecipe nms) {
        return new Bukkit2NMSFurnaceRecipe(bukkit.modify(nms.getBukkitRecipe()));
    }

    @Override
    public FurnaceModifier getBukkitModifier() {
        return bukkit;
    }

}
