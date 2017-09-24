package com.gmail.jannyboy11.customrecipes.impl.furnace.custom.modify;

import java.util.function.Function;

import com.gmail.jannyboy11.customrecipes.api.furnace.modify.FurnaceModifier;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;

//TODO make more implementations :D
public interface NMSFurnaceModifier<T extends NMSFurnaceRecipe, R extends NMSFurnaceRecipe> extends Function<T, R>{
    
    public R modify(T t);
    
    public FurnaceModifier getBukkitModifier();
    
    @Override
    public default R apply(T t) {
        return modify(t);
    }

}
