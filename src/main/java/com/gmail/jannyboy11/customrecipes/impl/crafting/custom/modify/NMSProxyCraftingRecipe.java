package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.inventory.Recipe;
import org.bukkit.util.Consumer;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.World;

public class NMSProxyCraftingRecipe implements IRecipe {

    private final BiPredicate<? super InventoryCrafting, ? super World> testInventory;
    private final Function<? super InventoryCrafting, ? extends ItemStack> craftItem;
    private final Supplier<? extends ItemStack> getResult;
    private final Function<? super InventoryCrafting, ? extends NonNullList<ItemStack>> takeItems;
    private final Supplier<? extends NonNullList<RecipeItemStack>> getIngredients;
    private final BooleanSupplier isHidden;
    private final Supplier<? extends Recipe> toBukkitRecipe;
    private final Consumer<? super MinecraftKey> setKey;

    public NMSProxyCraftingRecipe(BiPredicate<? super InventoryCrafting, ? super World> testInventory,
            Function<? super InventoryCrafting, ? extends ItemStack> craftItem,
            Supplier<? extends ItemStack> getResult,
            Function<? super InventoryCrafting, ? extends NonNullList<ItemStack>> takeItems,
            Supplier<? extends NonNullList<RecipeItemStack>> getIngredients,
            BooleanSupplier isHidden,
            Supplier<? extends Recipe> toBukkitRecipe,
            Consumer<? super MinecraftKey> setKey) {

        this.testInventory = Objects.requireNonNull(testInventory);
        this.craftItem = Objects.requireNonNull(craftItem);
        this.getResult = Objects.requireNonNull(getResult);
        this.takeItems = Objects.requireNonNull(takeItems);
        this.getIngredients = Objects.requireNonNull(getIngredients);
        this.isHidden = Objects.requireNonNull(isHidden);
        this.toBukkitRecipe = Objects.requireNonNull(toBukkitRecipe);
        this.setKey = Objects.requireNonNull(setKey);
    }

    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
        return testInventory.test(inventoryCrafting, world);
    }

    @Override
    public ItemStack b() {
        return getResult.get();
    }

    @Override
    public NonNullList<ItemStack> b(InventoryCrafting inventoryCrafting) {
        return takeItems.apply(inventoryCrafting);
    }

    @Override
    public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
        return craftItem.apply(inventoryCrafting);
    }

    @Override
    public void setKey(MinecraftKey key) {
        setKey.accept(key);
    }

    @Override
    public Recipe toBukkitRecipe() {
        return toBukkitRecipe.get();
    }
    
    @Override
    public NonNullList<RecipeItemStack> d() {
        return getIngredients.get();
    }
    
    @Override
    public boolean c() {
        return isHidden.getAsBoolean();
    }

}
