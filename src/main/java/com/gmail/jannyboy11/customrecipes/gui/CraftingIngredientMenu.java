package com.gmail.jannyboy11.customrecipes.gui;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.util.GridView;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.BackButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ItemButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

public class CraftingIngredientMenu extends MenuHolder<CustomRecipesPlugin> {
    
    private static final String[] ASCII_LAYOUT = new String[] {
            "PPPBMMMMM",
            "PIPBMMMMM",
            "PPPBMMMMM",
            "CRSBMMMMM",
    };
    
    private static final ItemStack BLUE_PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).name("U Can't Touch This").durability(DyeColor.LIGHT_BLUE.getWoolData()).build();
    private static final ItemStack BLACK_PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).name("Triggered!!!11!!1!!!!").durability(DyeColor.BLACK.getWoolData()).build();
    private static final ItemStack CANCEL = new ItemBuilder(Material.CONCRETE).durability(DyeColor.RED.getWoolData()).build();
    private static final ItemStack SAVE = new ItemBuilder(Material.CONCRETE).durability(DyeColor.LIME.getWoolData()).build();
    
    
    private final Map<Character, ? extends MenuButton> legend;
    
    private final ItemStack ingredient;
    private final Set<IngredientModifier> activeModifiers;    

    public CraftingIngredientMenu(CustomRecipesPlugin plugin, ItemStack ingredient,
            Set<IngredientModifier> activeModifiers, Supplier<? extends Inventory> backTo) {
        super(plugin, 4 * 9, "Edit ingredient");
        
        this.ingredient = ingredient;
        this.activeModifiers = activeModifiers;
        
        this.legend = Map.of(
                'P', new ItemButton(BLUE_PANE),
                'B', new ItemButton(BLACK_PANE),
                'C', new ItemButton(CANCEL),    //TODO custom implementation
                'S', new ItemButton(SAVE),      //TODO custom implementation
                'R', new BackButton(backTo)
                );
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        GridView grid = new GridView(event.getInventory(), 9, 4);
        
        
    }
    
    
}
