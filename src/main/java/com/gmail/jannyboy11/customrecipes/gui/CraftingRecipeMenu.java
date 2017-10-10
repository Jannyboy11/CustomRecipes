package com.gmail.jannyboy11.customrecipes.gui;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.BackButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ItemButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.RedirectItemButton;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;
import com.gmail.jannyboy11.customrecipes.util.inventory.GridView;

import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CraftingRecipeMenu extends MenuHolder<CustomRecipesPlugin> {

    private static final String[] ASCII_LAYOUT = new String[] {
          "PPPPPPPPP",
          "P   PPPPP",
          "P   PP PP",
          "P   PPPPP",
          "PPPPPPPPP",
          "PPDPBPEPP",
    };
    private static final ItemStack DELETE_BUTTON = new ItemBuilder(Material.BARRIER).name("Delete").build();
    private static final ItemStack EDIT_BUTTON = new ItemBuilder(Material.STRUCTURE_VOID).name("Edit").build(); 
    private static final ItemStack BORDER_BUTTON = new ItemBuilder(Material.STAINED_GLASS_PANE)
            .name("U Can't Touch This")
            .durability(DyeColor.LIGHT_BLUE.getWoolData())
            .build();
    
    private final Map<Character, ? extends MenuButton> legend;
    private final CraftingRecipe recipe;
    
    public CraftingRecipeMenu(CustomRecipesPlugin plugin, CraftingRecipe craftingRecipe, Supplier<? extends Inventory> backRedirect) {
        super(plugin, 6 * 9, "Manage " + craftingRecipe.getKey());
        
        this.recipe = craftingRecipe;
        this.legend = Map.of(
                'P', new ItemButton(BORDER_BUTTON),
                'D', new ItemButton(DELETE_BUTTON), //TODO use custom implementation that deletes the recipe and redirects
                'E', new RedirectItemButton(EDIT_BUTTON, () -> new CraftingRecipeEditor(getPlugin(), recipe).getInventory()),
                'B', new BackButton(backRedirect));
    }
    
    @Override
    public void onOpen(InventoryOpenEvent event) {
        for (int y = 0; y < ASCII_LAYOUT.length; y++) {
            for (int x = 0; x < ASCII_LAYOUT[y].length(); x++) {
                MenuButton button = legend.get(ASCII_LAYOUT[y].charAt(x));
                if (button != null) setButton(y * 9 + x, button);
            }
        }
        
        layoutRecipe();
    }
    
    
    private void layoutRecipe() {
        //TODO animated ingredients for choice ingredients with multiple choices? like the recipe book?
        //TODO the animation task can be cancelled on close
        
        GridView gridView = new GridView(getInventory(), 9, 5);
        
        //layout result
        gridView.setItem(6, 2, recipe.getResult());
        
        //layout ingredients
        if (recipe instanceof ShapedRecipe) { //TODO should check whether the base is a ShapedRecipe tho.
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            
            int width = shapedRecipe.getWidth();
            int height = shapedRecipe.getHeight();
            
            int i = 0;
            
            int addW = width == 1 ? 1 : 0;
            int addH = height == 1 ? 1 : 0;
            
            for (int h = addH; h < height + addH; h++) {
                if (i >= recipe.getIngredients().size()) break;
                
                for (int w = addW; w < width + addW; w++) {
                    if (i >= recipe.getIngredients().size()) break;
                    
                    ChoiceIngredient choiceIngredient = shapedRecipe.getIngredients().get(i);
                    if (!choiceIngredient.getChoices().isEmpty()) {
                        ItemStack firstChoice = choiceIngredient.getChoices().get(0);
                        
                        gridView.setItem(1 + w, 1 + h, firstChoice);
                    }
                    
                    i++;
                }
            }
            
        } else {
            //not a shaped recipe
            
            int i = 0;
            for (int h = 0; h < 3; h++) {
                if (i >= recipe.getIngredients().size()) break;
                
                for (int w = 0; w < 3; w++) {
                    if (i >= recipe.getIngredients().size()) break;
                    
                    Ingredient ingredient = recipe.getIngredients().get(i);
                    if (!(ingredient instanceof ChoiceIngredient)) continue;
                    
                    ChoiceIngredient choiceIngredient = (ChoiceIngredient) ingredient;
                    
                    if (!choiceIngredient.getChoices().isEmpty()) {
                        ItemStack firstChoice = choiceIngredient.getChoices().get(0);
                        
                        gridView.setItem(1 + w, 1 + h, firstChoice);
                    }
                    
                    i++;
                }
            }
        }
    }
    
}
