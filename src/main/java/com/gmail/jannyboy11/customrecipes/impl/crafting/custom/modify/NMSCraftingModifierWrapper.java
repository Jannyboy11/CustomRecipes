package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import java.util.Objects;
import java.util.function.Supplier;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public abstract class NMSCraftingModifierWrapper
    <T extends IRecipe, R extends IRecipe,
    NT extends NMSCraftingRecipe<T>, RT extends NMSCraftingRecipe<R>>
    implements NMSCraftingModifier<NT, RT> {
    
    private final NMSCraftingModifier<T, R> internal;
    private final Supplier<? extends MinecraftKey> keySupplier;
    protected CraftingModifier bukkit;
    
    public NMSCraftingModifierWrapper(NMSCraftingModifier<T, R> internal, Supplier<? extends MinecraftKey> keySupplier) {
        this.internal = Objects.requireNonNull(internal);
        this.keySupplier = Objects.requireNonNull(keySupplier);
    }

    @Override
    public RT modify(NT base) {
        T t = base.getHandle();
        R r = internal.apply(t);
        //TODO can we do better?
        return (RT) new NMSCraftingRecipe<R>(r) {
            @Override
            public MinecraftKey getKey() {
                return keySupplier.get();
            }
        };
    }

    @Override
    public CraftingModifier getBukkitModifier() {
        return bukkit == null ? bukkit = createBukkitModifier() : bukkit;
    }
    
    protected abstract CraftingModifier createBukkitModifier();

}
