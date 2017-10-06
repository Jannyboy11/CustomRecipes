package com.gmail.jannyboy11.customrecipes.gui.container;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;

import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.EntityHuman;

public class CraftingRecipeContainer extends Container {
    
    public CraftingRecipeContainer(int id) {
        this.windowId = id;
        
    }

    @Override
    public InventoryView getBukkitView() {
        //TODO
        return new CraftInventoryView(/*HumanEntity*/ null, /*opened inventory*/null, this);
    }

    @Override
    public boolean canUse(EntityHuman arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}
