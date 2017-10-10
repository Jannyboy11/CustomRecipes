package com.gmail.jannyboy11.customrecipes.gui.container;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;

import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Slot;

public class FixedSlot extends Slot {
    
    /*
     * X and Y positions visualised
     * 
     *      y\x     8   26  44  62  80  98  116 134 152
     *      
     *      18      0   1   2   3   4   5   6   7   8
     *      36      9   10  11  12  13  14  15  16  17
     *      54      18  19  20  21  22  23  24  25  26
     *      62      27  28  29  30  31  32  33  34  35
     *      80      36  37  38  39  40  41  42  43  44
     *      108     45  46  47  48  49  50  51  52  53
     * 
     * Minecraft WHY?!?!
     */

    public FixedSlot(IInventory inventory, int index, int xPosition, int yPosition) {
        super(inventory, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isAllowed(EntityHuman player) {
        return false;
    }
    
    @Override
    public boolean isAllowed(ItemStack input) {
        return false;
    }
    
    @Override
    public void set(ItemStack item) {
        //we are not setting any item, this is a fixed slot!
        this.f();
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
    }

}
