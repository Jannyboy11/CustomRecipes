package com.gmail.jannyboy11.customrecipes.impl.furnace;

import java.util.Iterator;
import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceManager;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.MapIterator;

//TODO more api methods
public class CRFurnaceManager implements FurnaceManager {
    
    private final NMSFurnaceManager nmsManager;
    
    public CRFurnaceManager(NMSFurnaceManager nmsManager) {
        this.nmsManager = Objects.requireNonNull(nmsManager);
    }

    @Override
    public Iterator<com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe> iterator() {
        return new MapIterator<>(nmsManager.iterator(), NMSFurnaceRecipe::getBukkitRecipe);
    }

    @Override
    public FurnaceRecipe addCustomRecipe(FurnaceRecipe furnaceRecipe) {
        NMSFurnaceRecipe nms = CRFurnaceRecipe.getNMSRecipe(furnaceRecipe);
        nmsManager.addCustomRecipe(nms);
        return nms.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe addVanillaRecipe(FurnaceRecipe furnaceRecipe) {
        NMSFurnaceRecipe nms = CRFurnaceRecipe.getNMSRecipe(furnaceRecipe);
        nmsManager.addVanillaRecipe(nms);
        return nms.getBukkitRecipe();
    }

    @Override
    public Iterator<? extends FurnaceRecipe> customIterator() {
        return new MapIterator<>(nmsManager.customIterator(), NMSFurnaceRecipe::getBukkitRecipe);
    }

    @Override
    public Iterator<? extends FurnaceRecipe> vanillaIterator() {
        return new MapIterator<>(nmsManager.vanillaIterator(), NMSFurnaceRecipe::getBukkitRecipe);
    }

    @Override
    public FurnaceRecipe getRecipe(ItemStack ingredient) {
        NMSFurnaceRecipe result = nmsManager.getRecipe(CraftItemStack.asNMSCopy(ingredient));
        return result == null ? null : result.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe getCustomRecipe(ItemStack ingredient) {
        NMSFurnaceRecipe result = nmsManager.getCustomRecipe(CraftItemStack.asNMSCopy(ingredient));
        return result == null ? null : result.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe getVanillaRecipe(ItemStack ingredient) {
        NMSFurnaceRecipe result = nmsManager.getCustomRecipe(CraftItemStack.asNMSCopy(ingredient));
        return result == null ? null : result.getBukkitRecipe();
    }

    @Override
    public void reset() {
        nmsManager.reset();
    }

    @Override
    public void clearVanilla() {
        nmsManager.clearVanilla();
    }

    @Override
    public void clearCustom() {
        nmsManager.clearCustom();
    }
    
    public void clear() {
        nmsManager.clear();
    }

    @Override
    public FurnaceRecipe removeRecipe(ItemStack ingredient) {
        NMSFurnaceRecipe recipe = nmsManager.removeRecipe(CraftItemStack.asNMSCopy(ingredient));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe removeVanillaRecipe(ItemStack ingredient) {
        NMSFurnaceRecipe recipe = nmsManager.removeVanillaRecipe(CraftItemStack.asNMSCopy(ingredient));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe removeCustomRecipe(ItemStack ingredient) {
        NMSFurnaceRecipe recipe = nmsManager.removeCustomRecipe(CraftItemStack.asNMSCopy(ingredient));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }
    
    
    @Override
    public FurnaceRecipe removeRecipe(NamespacedKey key) {
        NMSFurnaceRecipe recipe = nmsManager.removeRecipe(CraftNamespacedKey.toMinecraft(key));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }
    
    @Override
    public FurnaceRecipe removeVanillaRecipe(NamespacedKey key) {
        NMSFurnaceRecipe recipe = nmsManager.removeVanillaRecipe(CraftNamespacedKey.toMinecraft(key));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe removeCustomRecipe(NamespacedKey key) {
        NMSFurnaceRecipe recipe = nmsManager.removeCustomRecipe(CraftNamespacedKey.toMinecraft(key));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }
    
    
    @Override
    public FurnaceRecipe getRecipe(NamespacedKey key) {
        NMSFurnaceRecipe recipe = nmsManager.getRecipe(CraftNamespacedKey.toMinecraft(key));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }
    
    @Override
    public FurnaceRecipe getCustomRecipe(NamespacedKey key) {
        NMSFurnaceRecipe recipe = nmsManager.getCustomRecipe(CraftNamespacedKey.toMinecraft(key));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }

    @Override
    public FurnaceRecipe getVanillaRecipe(NamespacedKey key) {
        NMSFurnaceRecipe recipe = nmsManager.getVanillaRecipe(CraftNamespacedKey.toMinecraft(key));
        return recipe == null ? null : recipe.getBukkitRecipe();
    }

}
