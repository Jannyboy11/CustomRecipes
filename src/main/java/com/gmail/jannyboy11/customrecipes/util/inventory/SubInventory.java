package com.gmail.jannyboy11.customrecipes.util.inventory;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;

public class SubInventory implements IInventory {
    
    private final IInventory source;
    private final int startIndex, stopIndex;
    
    public SubInventory(IInventory source, int startIndex, int stopIndex) {
        this.source = Objects.requireNonNull(source);
        
        if (startIndex > stopIndex) throw new IllegalArgumentException("start cannot be greater than stop!");
        if (startIndex < 0) throw new IllegalArgumentException("start cannot be below zero!");
        if (stopIndex > source.getSize()) throw new IllegalArgumentException("stop cannot be greater than the source size!");
        
        this.startIndex = startIndex;
        this.stopIndex = stopIndex;
    }
    
    private int adaptSlot(int slot) {
        return slot - startIndex;
    }

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return source.getScoreboardDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return source.hasCustomName();
    }

    @Override
    public boolean a(EntityHuman player) { //can access
        return source.a(player);
    }

    @Override
    public boolean b(int slot, ItemStack stack) { //can move into slot
        return source.b(adaptSlot(slot), stack);
    }

    @Override
    public void clear() {
        IntStream.range(startIndex, stopIndex).forEach(i -> source.setItem(i, ItemStack.a));
    }

    @Override
    public void closeContainer(EntityHuman player) {
        source.closeContainer(player);
    }

    @Override
    public List<ItemStack> getContents() {
        return source.getContents().subList(startIndex, stopIndex);
    }

    @Override
    public ItemStack getItem(int index) {
        return source.getItem(adaptSlot(index));
    }

    @Override
    public Location getLocation() {
        return source.getLocation();
    }

    @Override
    public int getMaxStackSize() {
        return source.getMaxStackSize();
    }

    @Override
    public InventoryHolder getOwner() {
        return source.getOwner(); //should i have a separate field for this?
    }


    @Override
    public int getSize() {
        return stopIndex - startIndex;
    }

    @Override
    public List<HumanEntity> getViewers() {
        return source.getViewers();
    }

    @Override
    public void onClose(CraftHumanEntity player) {
        source.onClose(player);
    }

    @Override
    public void onOpen(CraftHumanEntity player) {
        source.onOpen(player);
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        source.setItem(adaptSlot(slot), item);
    }

    @Override
    public void setMaxStackSize(int slot) {
        source.setMaxStackSize(adaptSlot(slot));
    }

    @Override
    public void setProperty(int key, int value) {
        source.setProperty(key, value);
    }
    
    @Override
    public int getProperty(int slot) {
        return source.getProperty(slot);
    }
    
    @Override
    public int h() { //property count
        return source.h();
    }

    @Override
    public ItemStack splitStack(int slot, int amount) {
        return source.splitStack(adaptSlot(slot), amount);
    }

    @Override
    public ItemStack splitWithoutUpdate(int slot) {
        return source.splitWithoutUpdate(adaptSlot(slot));
    }

    @Override
    public void startOpen(EntityHuman player) {
        source.startOpen(player);
    }

    @Override
    public void update() {
        source.update();
    }

    @Override
    public boolean x_() { //isEmpty
        return getContents().stream().allMatch(ItemStack::isEmpty);
    }

}
