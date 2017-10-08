package com.gmail.jannyboy11.customrecipes.gui;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.gui.framework.GuiInventoryHolder;

import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.ITileEntityContainer;
import net.minecraft.server.v1_12_R1.InventorySubcontainer;
import net.minecraft.server.v1_12_R1.PlayerInventory;

public class CraftingRecipeEditor extends GuiInventoryHolder {
    
    /*
     * Legend:
     * 
     * G - light blue stained glass pane - may become 6x6 in the future
     * P - light blue stained glass pane
     * B - 'Base' base recipe selector redirect
     * M - 'Modifiers' modifiers selector redirect
     * C - 'Cancel' resets the recipe
     * B - 'Back'
     * S - 'Save'
     * space - will be filled in by the recipe - ingredients will flicker with minecraft:structure_void
     */
    private static final String[] asciiLayout = new String[] {
            "GGGGGGPPP",
            "G   GGP P",
            "G   GGPPP",
            "G   GGPBM",
            "GGGGGGPPP",
            "GGGGGGCRS",            
    };

    private CraftingRecipeEditor(EditorInventory inventory) {
        super(inventory.getPlugin(), inventory.getBukkitInventory());
    }
    
    public CraftingRecipeEditor(CustomRecipesPlugin plugin, CraftingRecipe recipe) {
        this(new EditorInventory(plugin, recipe));
    }
    
    @Override
    public void onOpen(InventoryOpenEvent event) {
        
    }
    
    @Override
    public void onClick(InventoryClickEvent event) {
        
    }
    
    private static class EditorInventory extends InventorySubcontainer implements ITileEntityContainer {
        
        private final CustomRecipesPlugin plugin;
        private final CraftInventory craftInventory;
        private CraftingRecipe recipe;
        
        public EditorInventory(CustomRecipesPlugin plugin, CraftingRecipe recipe) {
            super("Edit " + recipe.getKey(), true, 6 * 9);
            this.plugin = plugin;
            this.recipe = recipe;
            this.craftInventory = new CraftInventory(this);
            this.bukkitOwner = new CraftingRecipeEditor(this);
        }
        
        public CustomRecipesPlugin getPlugin() {
            return plugin;
        }
        
        public CraftInventory getBukkitInventory() {
            return craftInventory;
        }

        @Override
        public net.minecraft.server.v1_12_R1.Container createContainer(PlayerInventory playerInventory, EntityHuman human) {
            EntityPlayer player = (EntityPlayer) human;
            return new Container(player.nextContainerCounter(), player.getBukkitEntity(), craftInventory);
        }
        
        @Override
        public String getContainerName() {
            return "minecraft:container"; //might need to change this to minecraft:chest
        }
    }
    
    private static class Container extends net.minecraft.server.v1_12_R1.Container {
        private final CraftInventoryView bukkitView;
        
        public Container(int id, CraftHumanEntity player, CraftInventory viewing) {
            this.windowId = id;
            this.bukkitView = new CraftInventoryView(player, viewing, this);
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

            //TODO I can now use a combination of CombinedInventory and SubInventory :D
            
            //TODO
            return net.minecraft.server.v1_12_R1.ItemStack.a;
        }
    }

}
