package com.gmail.jannyboy11.customrecipes.gui;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.gui.container.FixedSlot;
import com.gmail.jannyboy11.customrecipes.gui.framework.GuiInventoryHolder;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;
import com.gmail.jannyboy11.customrecipes.util.inventory.GridView;

import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.ITileEntityContainer;
import net.minecraft.server.v1_12_R1.InventorySubcontainer;
import net.minecraft.server.v1_12_R1.PlayerInventory;
import net.minecraft.server.v1_12_R1.Slot;

public class CraftingRecipeEditor extends GuiInventoryHolder {
    
    /*
     * Legend:
     * 
     * G - light blue stained glass pane - may become 6x6 in the future
     * P - light blue stained glass pane
     * B - 'Base' base recipe selector redirect
     * M - 'Modifiers' modifiers selector redirect
     * C - 'Cancel' resets the recipe
     * R - 'Return'
     * S - 'Save'
     * space - will be filled in by the recipe - ingredients will flicker with minecraft:structure_void
     */
    private static final String[] ASCII_LAYOUT = new String[] {
            "GGGGGGPPP",
            "G   GGP P",
            "G   GGPPP",
            "G   GGPBM",
            "GGGGGGPPP",
            "GGGGGGCRS",            
    };
    
    private static final ItemStack BLUE_GLASS_PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).name("U Can't Touch This").durability(DyeColor.LIGHT_BLUE.getWoolData()).build();
    private static final ItemStack RIGHT_CLICK_TO_EDIT = new ItemBuilder(Material.STRUCTURE_VOID).name("Right click to edit!").build();
    private static final ItemStack SAVE = new ItemBuilder(Material.CONCRETE).name("Save").durability(DyeColor.LIME.getWoolData()).build();
    private static final ItemStack CANCEL = new ItemBuilder(Material.CONCRETE).name("Cancel").durability(DyeColor.RED.getWoolData()).build();
    private static final ItemStack RETURN = new ItemBuilder(Material.WOOD_DOOR).name("Back").build();
    private static final ItemStack BASE = new ItemBuilder(Material.ENDER_PEARL).name("Base").build();
    private static final ItemStack MODIFIERS = new ItemBuilder(Material.EYE_OF_ENDER).name("Modifiers").build();

    private static final Map<Character, ItemStack> LEGEND = Map.of(
            'G', BLUE_GLASS_PANE,
            'P', BLUE_GLASS_PANE,
            'B', BASE,
            'M', MODIFIERS,
            'C', CANCEL,
            'R', RETURN,
            'S', SAVE);
    
    private static final Set<Integer> TOP_SHIFT_CLICKABLE_SLOTS = new LinkedHashSet<>();
    static {final int addY = 1;
        final int addX = 1;
        
        for (int y = addY; y < 3 + addY; y++) {
            for (int x = addX; x < 3 + addX; x++) {
                TOP_SHIFT_CLICKABLE_SLOTS.add(y * 9 + x); //ingredient slots
            }
        }
        
        TOP_SHIFT_CLICKABLE_SLOTS.add(1 * 9 + 7); //result slots
    }
    
    
    private CraftingRecipeEditor(EditorInventory inventory) {
        super(inventory.getPlugin(), inventory.getBukkitInventory());
    }
    
    public CraftingRecipeEditor(CustomRecipesPlugin plugin, CraftingRecipe recipe) {
        this(new EditorInventory(plugin, recipe));
    }
    
    @Override
    public CREditorInventory getInventory() {
        return (CREditorInventory) super.getInventory();
    }
    
    public CraftingRecipe getRecipe() {
        return getInventory().getRecipe();
    }
    
    private void setRecipe(CraftingRecipe recipe) {
        //TODO implement this
        //TODO make the save button call this method
    }
    
    
    @Override
    public void onOpen(InventoryOpenEvent event) {
        GridView grid = new GridView(getInventory(), 9, 6);
        
        //lay out basic structure
        for (int y = 0; y < ASCII_LAYOUT.length; y++) {
            for (int x = 0; x < ASCII_LAYOUT[y].length(); x++) {
                char c = ASCII_LAYOUT[y].charAt(x);
                ItemStack stack = LEGEND.get(c);
                if (stack != null) grid.setItem(x, y, stack);
            }
        }
        
        //TODO layout recipe? //TODO make it flicker (using packets)!
        Recipe recipe = getRecipe();
        
    }
    
    @Override
    public void onClose(InventoryCloseEvent event) {
        //TODO stop the flickering task.
        //TODO is there more stuff?
    }
    
    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(false);

        switch(event.getClick()) {
        case LEFT:
            //TODO implement buttons for just regular left clicks
            
            break;
        case SHIFT_LEFT:
        case SHIFT_RIGHT:
            break;
        }
        
        getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> ((org.bukkit.entity.Player) event.getWhoClicked()).updateInventory());
    }
    
    private static class CREditorInventory extends CraftInventory {

        public CREditorInventory(EditorInventory inventory) {
            super(inventory);
        }
        
        @Override
        public EditorInventory getInventory() {
            return (EditorInventory) this.inventory;
        }
        
        public CraftingRecipe getRecipe() {
            return getInventory().getRecipe();
        }
        
        public CustomRecipesPlugin getPlugin() {
            return getInventory().getPlugin();
        }
    }
    
    private static class EditorInventory extends InventorySubcontainer implements ITileEntityContainer {
        
        private final CustomRecipesPlugin plugin;
        private final CREditorInventory bukkitInventory;
        private CraftingRecipe recipe;
        
        public EditorInventory(CustomRecipesPlugin plugin, CraftingRecipe recipe) {
            super("Edit " + recipe.getKey(), true, 6 * 9);
            this.plugin = plugin;
            this.recipe = recipe;
            this.bukkitInventory = new CREditorInventory(this);
            this.bukkitOwner = new CraftingRecipeEditor(this);
        }
        
        public CustomRecipesPlugin getPlugin() {
            return plugin;
        }
        
        public CREditorInventory getBukkitInventory() {
            return bukkitInventory;
        }

        @Override
        public Container createContainer(PlayerInventory playerInventory, EntityHuman human) {
            EntityPlayer player = (EntityPlayer) human;
            return new Container(player.nextContainerCounter(), player.getBukkitEntity(), bukkitInventory);
        }
        
        @Override
        public String getContainerName() {
            return "minecraft:container";
        }
        
        private void setRecipe(CraftingRecipe recipe) {
            this.recipe = recipe;
        }
        
        public CraftingRecipe getRecipe() {
            return recipe;
        }
    }
    
    private static class Container extends net.minecraft.server.v1_12_R1.Container {
        
        private final CustomRecipesPlugin plugin;
        private final CraftInventoryView bukkitView;
        private final EditorInventory editorInventory;
        private final PlayerInventory playerInventory;
        
        public Container(int id, CraftHumanEntity player, CREditorInventory viewing) {
            this.windowId = id;
            this.bukkitView = new CraftInventoryView(player, viewing, this);
            this.editorInventory = viewing.getInventory();
            this.plugin = viewing.getPlugin();
            
            EntityHuman entityHuman = player.getHandle();
            EditorInventory editorInventory = viewing.getInventory();
            editorInventory.startOpen(entityHuman);
            this.playerInventory = (player.getHandle().inventory);
            
            
            //setup slots //mostly copied from ContainerChest TODO should I extends ContainerChest?
            int y;
            for (y = 0; y < 6; y++) {
                for (int x = 0; x < 9; x++) {
                    int index = x + y * 9;
                    int xPosition = 8 + x * 18;
                    int yPosition = 18 + y * 18;
                    boolean isShiftClickSlot = isTopShiftClickSlot(index);
                    
                    if (isShiftClickSlot) {
                        a(new Slot(editorInventory, index, xPosition, yPosition));
                    } else {
                        a(new FixedSlot(editorInventory, index, xPosition, yPosition));
                    }
                }
            }
            
            //bottom slots
            final int i = (6 - 4) * 18; //(rows - 4) * 18 = 2 * 18 = 36
            for (y = 0; y < 3; y++) {
                for (int x = 0; x < 9; x++) {
                    int index = x + y * 9 + 9;      //+9 skips the hotbar, we do that later
                    int xPos = 8 + x * 18;          //i don't fucking know what this is for, it seems rudimentary
                    int yPos = 103 + y * 18 + i;    //i don't fucking know what this is for, it seems rudimentary
                    a(new Slot(playerInventory, index, xPos, yPos));
                }
            }
            //hotbar slots are player's first 9 slots, but in the container they're last.
            for (int x = 0; x < 9; x++) {
                a(new Slot(playerInventory, x, 8 + x * 18, 161 + i));
            }
        }
        
        private static boolean isTopShiftClickSlot(int rawSlotId) {
            return TOP_SHIFT_CLICKABLE_SLOTS.contains(rawSlotId);
        }

        @Override
        public InventoryView getBukkitView() {
            return bukkitView;
        }
        
        @Override
        public boolean c(EntityHuman player) { //can do transaction
            
            //is called on window clicks and checks whether player can go on
            
            //I could do permission checks here
            //The difference with canUse is, canUse is called way more often (every tick i think)
            
            //I might actually just return true here,
            //since players who got this inventory opened should always be allowed to click on this container
            
            return super.c(player); //superclass implementation checks whether the player is not blocked, which is fine for us.
        }

        @Override
        public boolean canUse(EntityHuman player) { //can use at all
            //looks like this one's called every tick
            return true;
        }
        
        @Override
        public net.minecraft.server.v1_12_R1.ItemStack shiftClick(EntityHuman entityhuman, int rawSlot) {            

            final Slot clickedSlot = slots.get(rawSlot);
            if (clickedSlot == null || !clickedSlot.hasItem()) return net.minecraft.server.v1_12_R1.ItemStack.a;
            
            net.minecraft.server.v1_12_R1.ItemStack clickedStack = clickedSlot.getItem();
            net.minecraft.server.v1_12_R1.ItemStack clickedClone = clickedStack.cloneItemStack();
            
            if (rawSlot < 6 * 9) {
                //top inventory was clicked, shift to bottom
                
                //can only shift-click on the ingredient slots and result slot
                if (!isTopShiftClickSlot(rawSlot)) return net.minecraft.server.v1_12_R1.ItemStack.a;
                
                //the indexes this item can merge into are 54 - 89 (bottom inventory)
                if (!this.a(clickedStack, 54, this.slots.size(), true)) {
                    return net.minecraft.server.v1_12_R1.ItemStack.a;
                }
                
            } else {
                //bottom inventory was clicked, shift to top
                
                /*
                 * Algorithm description:
                 * 
                 * 1. fill ingredient slots first, then the result slot
                 * 2. empty slots should take priority and fill up with count 1, after that try to merge with other stacks
                 * 
                 */
                boolean somethingTransferred = false;
                
                //iterator is ordered, because of LinkedHashSet
                Iterator<Integer> topSlotIterator = TOP_SHIFT_CLICKABLE_SLOTS.iterator();
                while (topSlotIterator.hasNext() && !clickedStack.isEmpty()) {
                    int topSlotIndex = topSlotIterator.next();
                    Slot topSlot = this.slots.get(topSlotIndex);
                    
                    //empty slots first
                    if (!topSlot.hasItem()) {
                        //no need to check whether the item is allowed, any item is allowed in the slot.
                        somethingTransferred = true;
                        
                        //clones the clicked stack and subtracts the argument (1)
                        //cloneAndSubtract returns a clone with the count equal to the argument (1)
                        topSlot.set(clickedStack.cloneAndSubtract(1));
                        topSlot.f();
                    }
                }
                
                topSlotIterator = TOP_SHIFT_CLICKABLE_SLOTS.iterator();
                while (topSlotIterator.hasNext() && !clickedStack.isEmpty()) {
                    int topSlotIndex = topSlotIterator.next();
                    Slot topSlot = this.slots.get(topSlotIndex);
                    
                    //merge with this slot
                    
                    //top slot has an item, guaranteed, because all empty slots are filled and our stack is still not empty
                    net.minecraft.server.v1_12_R1.ItemStack topSlotStack = topSlot.getItem();
                    
                    boolean canMerge = topSlotStack.getCount() < topSlot.getMaxStackSize(topSlotStack);
                    canMerge &= (clickedStack.getItem() == topSlotStack.getItem());
                    if (clickedStack.usesData()) canMerge &= (clickedStack.getData() == topSlotStack.getData());
                    canMerge &= Objects.equals(clickedStack.getTag(), topSlotStack.getTag());                    
                    
                    if (canMerge) {
                        somethingTransferred = true;
                        
                        int amountTransfer = Math.min(clickedStack.getCount(), topSlot.getMaxStackSize(clickedStack) - topSlotStack.getCount());
                        
                        topSlotStack.add(amountTransfer);                        
                        clickedStack.subtract(amountTransfer);
                        
                        topSlot.f();
                    }
                }
                
                if (!somethingTransferred) {
                    return net.minecraft.server.v1_12_R1.ItemStack.a;
                }
            }
            
            if (clickedSlot.hasItem()) {
                clickedSlot.f();
            } else {
                clickedSlot.set(net.minecraft.server.v1_12_R1.ItemStack.a);
            }
            
            return clickedClone;
        }
    }

}
