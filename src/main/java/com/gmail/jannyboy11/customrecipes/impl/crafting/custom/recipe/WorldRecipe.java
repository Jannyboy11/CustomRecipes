package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import java.util.Objects;
import java.util.UUID;

import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.World;

//TODO make a modifier for this
public class WorldRecipe extends ShapedRecipes {

    private final UUID world;

    public WorldRecipe(String group, int width, int heigth, NonNullList<RecipeItemStack> ingredients, ItemStack result, UUID world) {
        super(group, width, heigth, ingredients, result);
        this.world = world;
    }

    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
        if (!Objects.equals(this.world, world.getDataManager().getUUID())) return false;

        return super.a(inventoryCrafting, world);
    }

    public UUID getWorld() {
        return world;
    }

}
