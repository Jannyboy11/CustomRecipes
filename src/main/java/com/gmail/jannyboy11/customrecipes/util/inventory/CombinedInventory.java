package com.gmail.jannyboy11.customrecipes.util.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;

public class CombinedInventory implements IInventory {
    
    private final List<HumanEntity> viewers = new ArrayList<>();
    private final List<? extends IInventory> builtFrom;

    public CombinedInventory(List<? extends IInventory> from) {
        if (from.size() < 1) throw new IllegalArgumentException("builtFrom list cannot be empty!");
        this.builtFrom = Objects.requireNonNull(from);
    }
    
    public CombinedInventory(IInventory... from) {
        this(List.of(from));
    }

    @Override
    public String getName() {
        return builtFrom.get(0).getName();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return builtFrom.get(0).getScoreboardDisplayName();
    }

    @Override
    public boolean hasCustomName() {
        return builtFrom.get(0).hasCustomName();
    }

    @Override
    public boolean a(EntityHuman player) {
        return true;
    }

    @Override
    public boolean b(int slot, ItemStack item) {
        return true;
    }

    @Override
    public void clear() {
        builtFrom.forEach(IInventory::clear);
    }

    @Override
    public void closeContainer(EntityHuman player) {
    }

    @Override
    public List<ItemStack> getContents() {
        return new ConcatList<>(builtFrom.stream()
                .map(IInventory::getContents)
                .collect(Collectors.toList()));
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0) throw new IndexOutOfBoundsException("Slot cannot be below 0!");
        
        int sizeAcc = 0;
        for (IInventory inv : builtFrom) {
            if (slot >= sizeAcc && slot < (sizeAcc += inv.getSize())) {
                return inv.getItem(slot - sizeAcc);
            }
        }
        
        throw new IndexOutOfBoundsException("Slot cannot be above " + sizeAcc + "!");
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public int getMaxStackSize() {
        return MAX_STACK;
    }

    @Override
    public InventoryHolder getOwner() {
        return null;
    }

    @Override
    public int getProperty(int arg0) {
        return 0;
    }

    @Override
    public int getSize() {
        return builtFrom.stream().mapToInt(IInventory::getSize).sum();
    }

    @Override
    public List<HumanEntity> getViewers() {
        return viewers;
    }

    @Override
    public int h() {
        return 0;
    }

    @Override
    public void onClose(CraftHumanEntity player) {
        viewers.remove(player);
    }

    @Override
    public void onOpen(CraftHumanEntity player) {
        viewers.add(player);
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        if (slot < 0) throw new IndexOutOfBoundsException("Slot cannot be below 0!");
        
        int sizeAcc = 0;
        for (IInventory inv : builtFrom) {
            int size = inv.getSize();
            if (slot >= sizeAcc && slot < sizeAcc + size) {
                inv.setItem(sizeAcc + slot, item);
                return;
            }
            sizeAcc += size;
        }
        
        throw new IndexOutOfBoundsException("Slot cannot be above " + sizeAcc + "!");
    }

    @Override
    public void setMaxStackSize(int size) {
    }

    @Override
    public void setProperty(int key, int value) {
    }

    @Override
    public ItemStack splitStack(int index, int amount) {
        ItemStack itemstack = net.minecraft.server.v1_12_R1.ContainerUtil.a(this.getContents(), index, amount);

        if (!itemstack.isEmpty()) {
            this.update();
        }

        return itemstack;
    }

    @Override
    public ItemStack splitWithoutUpdate(int slot) {
        ItemStack itemstack = (ItemStack) this.getContents().get(slot);

        if (itemstack.isEmpty()) {
            return ItemStack.a;
        } else {
            this.getContents().set(slot, ItemStack.a);
            return itemstack;
        }
    }

    @Override
    public void startOpen(EntityHuman arg0) {
    }

    @Override
    public void update() {
        builtFrom.forEach(IInventory::update);
    }

    @Override
    public boolean x_() {
        return builtFrom.stream().allMatch(IInventory::x_);
    }
    
}
