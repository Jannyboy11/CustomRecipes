package com.gmail.jannyboy11.customrecipes.api.crafting.modify;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.Shape;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;

public interface ShapeModifier extends CraftingModifier<ShapedRecipe, ShapedRecipe> {
    
    public Shape reshape(Shape oldShape);
    
    @Override
    public default ShapedRecipe modify(ShapedRecipe base) {
        Shape newShape = reshape(base.getShape());
        return new ShapedRecipe() {
            
            @Override
            public boolean matches(CraftingInventory craftingInventory, World world) {
                return base.matches(craftingInventory, world);
            }

            @Override
            public ItemStack craftItem(CraftingInventory craftingInventory) {
               return base.craftItem(craftingInventory);
            }
            
            @Override
            public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
                return base.getLeftOverItems(craftingInventory);
            }

            @Override
            public ItemStack getResult() {
                return base.getResult();
            }

            @Override
            public boolean isHidden() {
                return base.isHidden();
            }
            
            @Override
            public String getGroup() {
                return base.getGroup();
            }

            @Override
            public NamespacedKey getKey() {
                return base.getKey(); //Should I be doing this? :thinking_face:
            }

            @Override
            public Shape getShape() {
                return newShape;
            }            
        };
    }

}
