package com.gmail.jannyboy11.customrecipes.gui.framework.menu;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class ToggleButton extends ItemButton {

    private boolean canToggle = true; 
    private boolean enabled;
    
    public ToggleButton(ItemStack item) {
        this(item, false);
    }
    
    public ToggleButton(ItemStack item, boolean enabled) {
        super(item);
        this.enabled = enabled;
        
        updateIcon();
    }
    
    @Override
    public void onClick(MenuHolder holder, InventoryClickEvent event) {
        toggle(holder.getPlugin());
        holder.getInventory().setItem(event.getSlot(), this.stack);
    }
    
    private void toggle(Plugin plugin) {
        if (!canToggle) return;
        if (!beforeToggle()) return;
        this.enabled = !isEnabled();
        updateIcon();
        afterToggle();
        canToggle = false;
        plugin.getServer().getScheduler().runTask(plugin, () -> canToggle = true);
    }
    
    public boolean beforeToggle() {
        return true;
    }
    
    public void afterToggle() {
    }
    
    public boolean isEnabled() {
        return enabled;
    }

    private void updateIcon() {
        this.stack = isEnabled() ? enable(getIcon()) : disable(getIcon());
    }
    
    private static ItemStack enable(ItemStack stack) {
        if (stack == null) return null;
        
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;

        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        
        stack.setItemMeta(meta);
        
        return stack;
    }
    
    private static ItemStack disable(ItemStack stack) {
        if (stack == null) return null;
        
        stack.getEnchantments().forEach((ench, level) -> stack.removeEnchantment(ench));
        
        return stack;
    }

}
