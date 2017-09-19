package com.gmail.jannyboy11.customrecipes.impl.modifier;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSModifiedCraftingRecipe
        <T extends IRecipe, R extends IRecipe, 
        NT extends NMSCraftingRecipe<T>, NR extends NMSCraftingRecipe<R>>

        extends NMSCraftingRecipe<NR> {
    
    private final NMSCraftingModifier<? super NT,? extends NR> modifier;
    private final NT base;
    
    public NMSModifiedCraftingRecipe(NMSCraftingModifier<? super NT,? extends NR> modifier, NT base) {
        super(modifier.apply(base));
        this.modifier = Objects.requireNonNull(modifier);
        this.base = Objects.requireNonNull(base);
    }
    
    public NT getBase() {
        return base;
    }
    
    public NMSCraftingModifier<? super NT, ? extends NR> getModifier() {
        return modifier;
    }

    @Override
    public MinecraftKey getKey() {
        return delegate.getKey();
    }
    
    @Override
    protected CRModifiedCraftingRecipe createBukkitRecipe() {
        return new CRModifiedCraftingRecipe(this);
    }
    
}
