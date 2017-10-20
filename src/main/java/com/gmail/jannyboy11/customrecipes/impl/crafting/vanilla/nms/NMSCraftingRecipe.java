package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import java.util.Objects;

import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingRecipe;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.World;

public abstract class NMSCraftingRecipe<R extends IRecipe> implements ExtendedCraftingRecipe {

    protected final R delegate;
    protected CraftingRecipe bukkit;
    
    @Deprecated
    protected NMSCraftingRecipe() {
        this.delegate = null;
    }
    
    public NMSCraftingRecipe(R delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }
    
    public CraftingRecipe getBukkitRecipe() {
        return bukkit == null ? bukkit = createBukkitRecipe() : bukkit;
    }
    
    protected CraftingRecipe createBukkitRecipe() {
        return new CRCraftingRecipe<>(this);
    }
    
    public abstract MinecraftKey getKey();
    
    public String getGroup() {
        return "";
    }
    
    public R getHandle() {
        return delegate;
    }
    
    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
        return delegate.a(inventoryCrafting, world);
    }
    
    @Override
    public boolean matches(InventoryCrafting inventory, World world) {
        return a(inventory, world);
    }

    @Override
    public ItemStack getResult() {
        return b();
    }
    
    @Override
    public ItemStack b() {
        return delegate.b();
    }
    
    @Override
    public NonNullList<ItemStack> getRemainders(InventoryCrafting inventory) {
        return b(inventory);
    }

    @Override
    public NonNullList<ItemStack> b(InventoryCrafting inventoryCrafting) {
        return delegate.b(inventoryCrafting);
    }

    @Override
    public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
        return delegate.craftItem(inventoryCrafting);
    }

    @Override
    public void setKey(MinecraftKey key) {
        delegate.setKey(key);        
    }

    @Override
    public Recipe toBukkitRecipe() {
        return delegate.toBukkitRecipe();
    }
    
    @Override
    public NonNullList<RecipeItemStack> d() {
        return delegate.d();
    }
    
    @Override
    public boolean isHidden() {
        return c();
    }
    
    @Override
    public boolean c() {
        return delegate.c();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this || o == delegate) return true;
        if (!(o instanceof IRecipe)) return false;
        
        if (o instanceof NMSCraftingRecipe) {
            NMSCraftingRecipe<?> that = (NMSCraftingRecipe<?>) o;
            return Objects.equals(this.delegate, that.delegate);
        }
        
        IRecipe that = (IRecipe) o;
        return Objects.equals(this.delegate, that);
    }
    
    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{delegate=" + delegate + "}";
    }

}
