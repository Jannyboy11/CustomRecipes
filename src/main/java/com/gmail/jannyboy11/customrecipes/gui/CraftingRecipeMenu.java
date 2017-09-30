package com.gmail.jannyboy11.customrecipes.gui;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftingRecipeMenu implements InventoryHolder {

    private static final ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE);
    static {
        GLASS_PANE.setTypeId(DyeColor.LIME.getWoolData());
    }

    private CustomRecipesPlugin plugin;
    private Inventory inventory;

    public CraftingRecipeMenu(CustomRecipesPlugin plugin, ItemStack[][] ingredientsGrid) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, "Manage Recipe");

        GridView gridView = new GridView(inventory, 9, 6);
        gridView.horizontalLine(3, 0, 4, GLASS_PANE);
        gridView.verticalLine(3, 0, 3, GLASS_PANE);

        int heigth = ingredientsGrid.length;
        int width = heigth == 0 ? 0 : ingredientsGrid[0].length;

        gridView.fill(0, 0, width, heigth, (x, y) -> ingredientsGrid[x][y]);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
