package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface RedirectButton<MH extends MenuHolder<?>> extends MenuButton<MH> {
    
    @Override
    public default void onClick(MH holder, InventoryClickEvent event) {
        holder.getPlugin().getServer().getScheduler().runTask(holder.getPlugin(), () -> {
           event.getView().close();
           
           HumanEntity player = event.getWhoClicked();
           player.openInventory(to());
        });
    }
    
    public Inventory to();

}
