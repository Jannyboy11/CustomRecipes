package com.gmail.jannyboy11.customrecipes.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.BackButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ItemButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.RedirectItemButton;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

public class CraftingManagerMenu extends MenuHolder<CustomRecipesPlugin> {
    
    private static final int RECIPES_PER_PAGE = 9 * 5;
    private static final ItemStack NEXT_BUTTON = new ItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA).name("Next").build();
    private static final ItemStack PREVIOUS_BUTTON = new ItemBuilder(Material.MAGENTA_GLAZED_TERRACOTTA).name("Previous").build();
    private static final ItemStack NEW_BUTTON = new ItemBuilder(Material.STRUCTURE_BLOCK).name("Create new").build();
    
    private final List<CraftingRecipe> recipes = new ArrayList<>();
    
    private int lastPageNr;
    private int pageNr;

    public CraftingManagerMenu(CustomRecipesPlugin plugin) {
        super(plugin, 9 * 6, "Manage Crafting Recipes");
    }
    
    @Override
    public void onOpen(InventoryOpenEvent event) {
        //refresh list every time this inventory is opened
        recipes.clear();
        getPlugin().getCraftingManager().spliterator().forEachRemaining(recipes::add);
        
        //recalculate number of pages
        lastPageNr = recipes.size() / RECIPES_PER_PAGE;
        
        //put in the buttons
        fillButtons();
        setButton(RECIPES_PER_PAGE + 3, new BackButton(() -> new MainMenu(getPlugin()).getInventory()));
        setButton(RECIPES_PER_PAGE + 5, new ItemButton(NEW_BUTTON)); //TODO redirect button to editor
    }
    
    private void fillButtons() {        
        int listIndex = pageNr * RECIPES_PER_PAGE;
        int inventoryIndex = 0;

        //fill buttons
        while (listIndex < recipes.size() && inventoryIndex < RECIPES_PER_PAGE) {
            CraftingRecipe recipe = recipes.get(listIndex);
            
            ItemStack icon = recipe.getResult();
            if (InventoryUtils.isEmptyStack(icon)) icon = new ItemStack(Material.STRUCTURE_VOID);
            icon = new ItemBuilder(icon).name(recipe.getKey().toString()).flags(ItemFlag.values()).build();

            MenuButton button = new RedirectItemButton(icon, () -> new CraftingRecipeMenu(getPlugin(), recipe, this::getInventory).getInventory());
            setButton(inventoryIndex, button);
            
            inventoryIndex++;
            listIndex++;
        }
        
        //fill empty slots till 45
        while (inventoryIndex < RECIPES_PER_PAGE) {
            if (!unsetButton(inventoryIndex)) getInventory().setItem(inventoryIndex, null);
            
            inventoryIndex++;
        }
        
        //buttons
        final int previousIndex = RECIPES_PER_PAGE;
        final int nextIndex = RECIPES_PER_PAGE + 8;
        
        if (pageNr < lastPageNr) {
            setButton(nextIndex, new NextButton());
        } else {
            unsetButton(nextIndex);
        }
        
        if (pageNr > 0) {
            setButton(previousIndex, new PreviousButton());
        } else {
            unsetButton(previousIndex);
        }
        
    }
    
    private static class NextButton extends ItemButton<CraftingManagerMenu> {
        public NextButton() {
            super(NEXT_BUTTON);
        }
        
        @Override
        public void onClick(CraftingManagerMenu cmm, InventoryClickEvent event) {
            cmm.pageNr++;
            cmm.fillButtons();
        }
    }
    
    private static class PreviousButton extends ItemButton<CraftingManagerMenu> {
        public PreviousButton() {
            super(PREVIOUS_BUTTON);
        }
        
        @Override
        public void onClick(CraftingManagerMenu cmm, InventoryClickEvent event) {
            cmm.pageNr--;
            cmm.fillButtons();
        }
    }
    
}
