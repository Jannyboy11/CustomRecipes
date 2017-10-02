package com.gmail.jannyboy11.customrecipes.gui;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;

import java.util.Map;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

//TODO use a Container that blocks non-button slots
public class CraftingRecipeMenu implements InventoryHolder {

    private static final String[] asciiLayout = new String[] {
          "PPPPPPPPP",
          "P   PPPPP",
          "P   PP PP",
          "P   PPPPP",
          "PPPPPPPPP",
          "PPDPBPEPP",
    };
    private static final Map<Character, ItemStack> legend = Map.of(
            'P', new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getWoolData()),
            ' ', new ItemStack(Material.AIR),
            'D', new ItemStack(Material.BARRIER),
            'E', new ItemStack(Material.STRUCTURE_VOID),
            'B', new ItemStack(Material.WOOD_DOOR));
    
    private CustomRecipesPlugin plugin;
    private Inventory inventory;
    
    //TODO support larger recipes than 3x3?

    public CraftingRecipeMenu(CustomRecipesPlugin plugin, ItemStack[][] ingredientsGrid, ItemStack result) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, "Manage Recipe");

        GridView gridView = new GridView(inventory, 9, 6);
        
        for (int y = 0; y < asciiLayout.length; y++) {
            for (int x = 0; x < asciiLayout[y].length(); x++) {
                char symbol = asciiLayout[y].charAt(x);
                ItemStack item = legend.get(symbol);
                gridView.setItem(x, y, item);
            }
        }

        int heigth = ingredientsGrid.length;
        int width = heigth == 0 ? 0 : ingredientsGrid[0].length;

        gridView.fill(0, 0, width, heigth, (x, y) -> ingredientsGrid[x][y]);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
