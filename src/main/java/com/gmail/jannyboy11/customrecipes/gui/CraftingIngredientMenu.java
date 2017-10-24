package com.gmail.jannyboy11.customrecipes.gui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.CraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.ModifiedCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.simple.SimpleChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.util.GridView;
import com.gmail.jannyboy11.customrecipes.api.util.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.BackButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ItemButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuButton;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.MenuHolder;
import com.gmail.jannyboy11.customrecipes.gui.framework.menu.ToggleButton;
import com.gmail.jannyboy11.customrecipes.impl.modify.CRModifierManager;
import com.gmail.jannyboy11.customrecipes.util.ItemBuilder;

public class CraftingIngredientMenu extends MenuHolder<CustomRecipesPlugin> {
    
    private static final String[] ASCII_LAYOUT = new String[] {
            "PPPBMMMMM",
            "PIPBMMMMM",
            "PPPBMMMMM",
            "CRSBMMMMM",
    };
    
    private static final ItemStack BLUE_PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).name("U Can't Touch This").durability(DyeColor.LIGHT_BLUE.getWoolData()).build();
    private static final ItemStack BLACK_PANE = new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").durability(DyeColor.BLACK.getWoolData()).build();
    private static final ItemStack CANCEL = new ItemBuilder(Material.CONCRETE).name("Cancel").durability(DyeColor.RED.getWoolData()).build();
    private static final ItemStack SAVE = new ItemBuilder(Material.CONCRETE).name("Save").durability(DyeColor.LIME.getWoolData()).build();
    private static final ItemStack MODIFIER = new ItemStack(Material.TOTEM);
    
    private final Map<Character, ? extends MenuButton> legend;
    
    private final List<? extends ItemStack> ingredientStacks;
    private final Set<NamespacedKey> activeModifiers;
    private final Consumer<CraftingIngredient> saveIngredientCallback;
    
    private CraftingIngredient lastSavedIngredient;
    
    
    public CraftingIngredientMenu(CustomRecipesPlugin plugin, CraftingIngredient ingredient,
            Consumer<CraftingIngredient> saveIngredientCallback, Supplier<? extends Inventory> backTo) {
        super(plugin, 4 * 9, "Edit ingredient");
        
        this.lastSavedIngredient = ingredient;
        this.saveIngredientCallback = saveIngredientCallback;
        this.ingredientStacks = ingredient instanceof ChoiceIngredient
                ? ((ChoiceIngredient) ingredient).getChoices()
                : ingredient.firstItemStack().stream().collect(Collectors.toList());
        this.activeModifiers = RecipeUtils.getAppliedModifiers(ingredient).stream()
                .map(CraftingIngredientModifier::getKey)
                .collect(Collectors.toSet());
        
        
        this.legend = Map.of(
                'P', new ItemButton(BLUE_PANE),
                'B', new ItemButton(BLACK_PANE),
                'C', new ResetModifiersButton(),
                'S', new SaveIngredientButton(),
                'R', new BackButton(backTo)
                );
    }
    
    private ItemStack getIngredientStack() {
        return ingredientStacks.isEmpty() ? null : ingredientStacks.get(0);
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
        
        grid.setItem(1, 1, getIngredientStack());//TODO make this a button that can add choices?
        
        layoutModifiers();
    }
    
    private void layoutModifiers() {
        GridView grid = new GridView(getInventory(), 9, 4);
        
        Iterator<Entry<NamespacedKey, Function<? super ItemStack, ? extends CraftingIngredientModifier>>> possibleIterator = getPlugin()
                .getModifierManager().getCraftingIngredientModifiers().entrySet().iterator();
        
        loopModifiers:
        for (int y = 0; y < 4; y++) {
            for (int x = 4; x < 9; x++) {
                if (possibleIterator.hasNext()) {
                    Entry<NamespacedKey, Function<? super ItemStack, ? extends CraftingIngredientModifier>> entry = possibleIterator.next();
                    
                    int slot = grid.getIndex(x, y);
                    NamespacedKey modifierKey = entry.getKey();
                    CraftingIngredientModifier modifier = entry.getValue().apply(getIngredientStack());
                    boolean isActiveModifier = activeModifiers.contains(modifierKey);
                    
                    //TODO make sure this is saved across multiple re-opens
                    ToggleButton button = new ToggleButton(new ItemBuilder(MODIFIER)
                            .name(modifierKey.toString())
                            .build(), isActiveModifier) {
                        @Override
                        public void afterToggle() {
                            if (isEnabled()) {
                                CraftingIngredientMenu.this.activeModifiers.add(modifierKey);
                            } else {
                                CraftingIngredientMenu.this.activeModifiers.remove(modifierKey);
                            }
                        }
                    };
                    
                    setButton(slot, button);
                    
                } else {
                    break loopModifiers;
                }
            }
        }
    }
    
    
    private static class SaveIngredientButton extends ItemButton<CraftingIngredientMenu> {

        public SaveIngredientButton() {
            super(SAVE);
        }
        
        @Override
        public void onClick(CraftingIngredientMenu holder, InventoryClickEvent event) {
            //TODO doesn't seem to work?
            holder.saveIngredientCallback.accept(holder.lastSavedIngredient = holder.makeNewIngredient());
        }
    }
    
    private static class ResetModifiersButton extends ItemButton<CraftingIngredientMenu> {

        public ResetModifiersButton() {
            super(CANCEL);
        }
        
        @Override
        public void onClick(CraftingIngredientMenu holder, InventoryClickEvent event) {
            //reset active modifiers
            holder.activeModifiers.clear();
            holder.activeModifiers.addAll(RecipeUtils.getAppliedModifiers(holder.lastSavedIngredient).stream()
                    .map(CraftingIngredientModifier::getKey)
                    .collect(Collectors.toList()));
            
            //update icons
            holder.layoutModifiers();
        }
    }
    
    private CraftingIngredient makeNewIngredient() {
        CraftingIngredient ingredient = SimpleChoiceIngredient.fromChoices(ingredientStacks
                .toArray(new ItemStack[ingredientStacks.size()])) ;
        ItemStack firstItem = getIngredientStack();
        
        CRModifierManager modifierManager = getPlugin().getModifierManager();
        Map<NamespacedKey, Function<? super ItemStack, ? extends CraftingIngredientModifier>> modifiers = modifierManager.getCraftingIngredientModifiers();
        for (NamespacedKey modifierKey : activeModifiers) {
            CraftingIngredient base = ingredient;
            //TODO can we use a proxy implementation?
            ingredient = new ModifiedCraftingIngredient<CraftingIngredient>() {
                CraftingIngredientModifier modifier = modifiers.get(modifierKey).apply(firstItem);
                CraftingIngredient result = modifier.modify(base);
                
                @Override
                public boolean isIngredient(ItemStack input) {
                    return result.isIngredient(input);
                }

                @Override
                public CraftingIngredient getBase() {
                    return base;
                }

                @Override
                public CraftingIngredientModifier getModifier() {
                    return modifier;
                }
            };
        }
        
        return ingredient;
    }
    
}
