package com.gmail.jannyboy11.customrecipes.gui;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.util.GridView;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.BackButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ItemButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ToggleButton;
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
    private static final ItemStack CANCEL = new ItemBuilder(Material.CONCRETE).name("Cancel").durability(DyeColor.RED.getWoolData()).build();
    private static final ItemStack SAVE = new ItemBuilder(Material.CONCRETE).name("Save").durability(DyeColor.LIME.getWoolData()).build();
    private static final ItemStack MODIFIER = new ItemStack(Material.TOTEM);
    
    private final Map<Character, ? extends MenuButton> legend;
    
    //TODO should probably hold a map of <Integer, NamespacedKey> to map inventory slots to modifiers
    private final ItemStack ingredientStack;
    private final Map<NamespacedKey, ? extends CraftingIngredientModifier> activeModifiers;    

    
    public CraftingIngredientMenu(CustomRecipesPlugin plugin, ItemStack ingredient,
            Map<NamespacedKey, ? extends CraftingIngredientModifier> activeModifiers, Supplier<? extends Inventory> backTo) {
        super(plugin, 4 * 9, "Edit ingredient");
        
        this.ingredientStack = ingredient;
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
        Inventory inventory = event.getInventory();
        GridView grid = new GridView(inventory, 9, 4);
        
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                char key = ASCII_LAYOUT[y].charAt(x);
                MenuButton button = legend.get(key);

                if (button != null) {
                    int slot = grid.getIndex(x, y);
                    setButton(slot, button);
                }                
            }
        }
        
        grid.setItem(1, 1, ingredientStack);
        
        layoutModifiers();
    }
    
    private void layoutModifiers() {
        GridView grid = new GridView(getInventory(), 9, 4);
        
        Iterator<? extends Entry<NamespacedKey, ? extends CraftingIngredientModifier>> iterator = activeModifiers.entrySet().iterator();
        loopModifiers:
        for (int y = 0; y < 4; y++) {
            for (int x = 4; x < 9; x++) {
                if (iterator.hasNext()) {
                    Entry<NamespacedKey, ? extends CraftingIngredientModifier> entry = iterator.next();
                    int slot = grid.getIndex(x, y);
                    NamespacedKey modifierKey = entry.getKey();
                    CraftingIngredientModifier modifier = entry.getValue();
                    
                    ToggleButton button = new ToggleButton(new ItemBuilder(MODIFIER)
                            .name(modifierKey.toString())
                            .build());
                    
                    setButton(slot, button);
                    
                } else {
                    break loopModifiers;
                }
            }
        }
    }
    
    
    
}
