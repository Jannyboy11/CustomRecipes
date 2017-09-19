package com.gmail.jannyboy11.customrecipes.impl.modifier;

import java.util.function.Function;

import com.gmail.jannyboy11.customrecipes.api.modifier.FurnaceModifier;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;

public interface NMSFurnaceModifier<T extends NMSFurnaceRecipe, R extends NMSFurnaceRecipe> extends Function<T, R>{
    
    public R modify(T t);
    
    public FurnaceModifier getBukkitModifier();
    
    @Override
    public default R apply(T t) {
        return modify(t);
    }

}
