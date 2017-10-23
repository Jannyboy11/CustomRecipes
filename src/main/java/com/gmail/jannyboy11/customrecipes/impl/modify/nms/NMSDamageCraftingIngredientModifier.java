package com.gmail.jannyboy11.customrecipes.impl.modify.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.MinecraftKey;

public class NMSDamageCraftingIngredientModifier implements ExtendedCraftingIngredientModifier<ExtendedCraftingIngredient, ExtendedCraftingIngredient> {

    public static final MinecraftKey KEY = new MinecraftKey("CustomRecipes", "crafting_damage_modifier");

    private final int damage;
    
    public NMSDamageCraftingIngredientModifier(int damage) {
        this.damage = damage;
    }
    
    @Override
    public ExtendedCraftingIngredient modify(ExtendedCraftingIngredient base) {
        return new ExtendedCraftingIngredientWrapper(base, itemStack -> itemStack.getData() == damage);
    }

    @Override
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public int getDamage() {
        return damage;
    }

}
