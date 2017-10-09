package com.gmail.jannyboy11.customrecipes.gui;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.event.inventory.ClickType;
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
    
    //TODO setter can be private, the save button is the only thing that may set the recipe.
    
    
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
        
        //TODO layout recipe? //TODO make it flicker!
        Recipe recipe = getRecipe();
        
    }
    
    @Override
    public void onClose(InventoryCloseEvent event) {
        //TODO stop the flickering task.
        //TODO is there more stuff?
    }
    
    @Override
    public void onClick(InventoryClickEvent event) {
        switch(event.getClick()) {
        case LEFT:
            //TODO implement buttons for just regular left clicks
            
            break;
        case SHIFT_LEFT:
        case SHIFT_RIGHT:
            event.setCancelled(false);
            break;
        }
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
            return "minecraft:container"; //might need to change this to minecraft:chest
        }
        
        private void setRecipe(CraftingRecipe recipe) {
            this.recipe = recipe;
        }
        
        public CraftingRecipe getRecipe() {
            return recipe;
        }
    }
    
    private static class Container extends net.minecraft.server.v1_12_R1.Container {
        private static final Set<Integer> TOP_SHIFT_CLICKABLE_SLOTS = new HashSet<>();
        static {final int addY = 1;
            final int addX = 1;
            
            for (int y = addY; y < 3 + addY; y++) {
                for (int x = addX; x < 3 + addX; x++) {
                    TOP_SHIFT_CLICKABLE_SLOTS.add(y * 9 + x); //ingredient slots
                }
            }
            
            TOP_SHIFT_CLICKABLE_SLOTS.add(1 * 9 + 8); //result slots
        }
        
        
        private final CraftInventoryView bukkitView;
        private final PlayerInventory playerInventory;
        
        public Container(int id, CraftHumanEntity player, CREditorInventory viewing) {
            this.windowId = id;
            this.bukkitView = new CraftInventoryView(player, viewing, this);
            
            EntityHuman entityHuman = player.getHandle();
            EditorInventory editorInventory = viewing.getInventory();
            editorInventory.startOpen(entityHuman);
            this.playerInventory = (player.getHandle().inventory);
            
            
            //setup slots //copied from ContainerChest. should I extend ContainerChest instead?
            int i = (6 - 4) * 18; //(rows - 4) * 18
            int j;

            for (j = 0; j < 6; j++) {
                for (int k = 0; k < 9; k++) {
                    int index = k + j * 9;
                    int xPosition = 8 + k * 18;
                    int yPosition = 18 + j * 18;
                    if (isTopShiftClickSlot(index)) {
                        a(new Slot(editorInventory, index, xPosition, yPosition));
                    } else {
                        a(new FixedSlot(editorInventory, index, xPosition, yPosition));
                    }
                }
            }
            for (j = 0; j < 3; j++) {
                for (int k = 0; k < 9; k++) {
                    a(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
                }
            }
            for (j = 0; j < 9; j++) {
                a(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
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
            CustomRecipesPlugin.getInstance().getLogger().info("DEBUG c(EntityHuman)");
            
            //WHAT DOES THIS DO? it is called on window clicks and checks whether player can go on?
            
            //I can do permission checks here, but I'm not sure what is the difference with canUse and this one.
            //I might actually just return true here for now, since players should always be allowed to click on this container
            return super.c(player); 
        }

        @Override
        public boolean canUse(EntityHuman player) { //can use at all
            CustomRecipesPlugin.getInstance().getLogger().info("DEBUG canUse(EntityHuman)");
            
            return true;
        }
        
        @Override
        public net.minecraft.server.v1_12_R1.ItemStack shiftClick(EntityHuman entityhuman, int rawSlot) {
            //TODO implement our fabulous shift click algorithm <3 we can use the method
            //protected boolean a(ItemStack itemstack, int startIndex, int endIndex, boolean toBottom); too
            
            if (rawSlot < 6 * 9 && !isTopShiftClickSlot(rawSlot)) return net.minecraft.server.v1_12_R1.ItemStack.a;
            
            //slot is either a shift-clickable top slot, or a bottom slot

            //TODO I can now use a combination of CombinedInventory and SubInventory?! :D
            net.minecraft.server.v1_12_R1.ItemStack itemClone = net.minecraft.server.v1_12_R1.ItemStack.a;
            Slot slot = this.slots.get(rawSlot);
            if (slot != null && slot.hasItem())
            {
                net.minecraft.server.v1_12_R1.ItemStack itemInSlot = slot.getItem();

                itemClone = itemInSlot.cloneItemStack();
                if (rawSlot < 6 * 9)
                {
                    if (!a(itemInSlot, 6 * 9, this.slots.size(), true)) {
                        return net.minecraft.server.v1_12_R1.ItemStack.a;
                    }
                } else if (!a(itemInSlot, 0, 6 * 9, false)) { //TODO change this? not every top slot is shift clickable.
                    return net.minecraft.server.v1_12_R1.ItemStack.a;
                }
                
                if (itemInSlot.isEmpty()) {
                    slot.set(net.minecraft.server.v1_12_R1.ItemStack.a);
                } else {
                    slot.f(); //not fully shift clicked, update the client's view of this slot
                }
            }
            
            return itemClone;
        }
    }

}
