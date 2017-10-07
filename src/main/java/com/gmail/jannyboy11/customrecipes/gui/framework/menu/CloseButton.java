package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

public class CloseButton<MH extends MenuHolder<?>> extends ItemButton<MH> {
    
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
    public void onClick(MH holder, InventoryClickEvent event) {
        holder.getPlugin().getServer().getScheduler().runTask(holder.getPlugin(), event.getView()::close);
    }

}
