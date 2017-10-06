package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

public class CloseButton extends ItemButton {
    
    public CloseButton() {
        this(Material.WOOD_DOOR);
    }
    
    public CloseButton(Material material) {
        this(material, "Close");
    }
    
    public CloseButton(Material material, String displayName) {
        super(new ItemBuilder(material).name(displayName).build());
    }
    
    @Override
    public <P extends Plugin, MH extends MenuHolder<P>> void onClick(MH holder, InventoryClickEvent event) {
        holder.getPlugin().getServer().getScheduler().runTask(holder.getPlugin(), event.getView()::close);
    }

}
