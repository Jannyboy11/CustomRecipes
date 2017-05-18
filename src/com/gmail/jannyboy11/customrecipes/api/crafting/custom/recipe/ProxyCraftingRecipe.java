package com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

public class ProxyCraftingRecipe implements CraftingRecipe {
	
	private final BiPredicate<? super CraftingInventory, ? super World> matcher;
	private final Function<? super CraftingInventory, ? extends ItemStack> crafter;
	private final Supplier<? extends ItemStack> result;
	private final Function<? super CraftingInventory, ? extends List<? extends ItemStack>> leftover;
	private final BooleanSupplier hidden;
	
	public ProxyCraftingRecipe(BiPredicate<? super CraftingInventory, ? super World> matcher,
			Function<? super CraftingInventory, ? extends ItemStack> crafter,
			Supplier<? extends ItemStack> result,
			Function<? super CraftingInventory, ? extends List<? extends ItemStack>> leftover,
			BooleanSupplier hidden) {
		
		this.matcher = Objects.requireNonNull(matcher);
		this.crafter = Objects.requireNonNull(crafter);
		this.result = Objects.requireNonNull(result);
		this.leftover = Objects.requireNonNull(leftover);
		this.hidden = Objects.requireNonNull(hidden);
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		return matcher.test(craftingInventory, world);
	}

	@Override
	public ItemStack craftItem(CraftingInventory craftingInventory) {
		return crafter.apply(craftingInventory);
	}

	@Override
	public ItemStack getResult() {
		return result.get();
	}

	@Override
	public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
		return leftover.apply(craftingInventory);
	}

	@Override
	public boolean isHidden() {
		return hidden.getAsBoolean();
	}

}
