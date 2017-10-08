package com.gmail.jannyboy11.customrecipes.util.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

//TODO
public class CombinedInventory implements Inventory {
    
    private final List<Inventory> builtFrom;
    
    public CombinedInventory(List<Inventory> builtFrom) {
        this.builtFrom = Objects.requireNonNull(builtFrom);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(int materialId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clear(int slot) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean contains(int materialId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(ItemStack itemStack) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(int materialId, int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(ItemStack itemStack, int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack itemStack, int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int first(int materialId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int first(Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int first(ItemStack itemStack) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int firstEmpty() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ItemStack[] getContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryHolder getHolder() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getItem(int slot) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxStackSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ItemStack[] getStorageContents() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<HumanEntity> getViewers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ListIterator<ItemStack> iterator(int startIndex) { //TODO what is this int?
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(int slot) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove(ItemStack stack) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setContents(ItemStack[] contents) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMaxStackSize(int slot) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStorageContents(ItemStack[] contents) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }
    
}
