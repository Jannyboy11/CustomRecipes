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
public class SubInventory implements Inventory {
    
    private final Inventory from;
    private final int starting, ending;
    
    public SubInventory(Inventory from, int startingIndex, int endingIndex) {
        this.from = Objects.requireNonNull(from);
        this.starting = startingIndex;
        this.ending = endingIndex;
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clear(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean contains(int arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Material arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(ItemStack arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Material arg0, int arg1) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(ItemStack arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack arg0, int arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int first(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int first(Material arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int first(ItemStack arg0) {
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
    public ItemStack getItem(int arg0) {
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
    public ListIterator<ItemStack> iterator(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove(Material arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove(ItemStack arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setContents(ItemStack[] arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setItem(int arg0, ItemStack arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMaxStackSize(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setStorageContents(ItemStack[] arg0) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

}
