package com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;

/**
 * A CraftingRecipe implementation which delegates all methods to the supplied functions in the constructor.
 * 
 * @author Jan
 */
public class ProxyRecipe implements CraftingRecipe {
	
	private final BiPredicate<? super CraftingInventory, ? super World> matcher;
	private final Function<? super CraftingInventory, ? extends ItemStack> crafter;
	private final Supplier<? extends ItemStack> result;
	private final Function<? super CraftingInventory, ? extends List<? extends ItemStack>> leftover;
	private final BooleanSupplier hidden;
	private final Supplier<NamespacedKey> key;
	
	/**
	 * Instantiate the ProxyRecipe with the supplied delegation functions.
	 * 
	 * @param matcher used in {@link ProxyRecipe#matches(CraftingInventory, World)}}
	 * @param crafter used in {@link ProxyRecipe#craftItem(CraftingInventory)}
	 * @param result used in {@link ProxyRecipe#getResult()}
	 * @param leftover used in {@link ProxyRecipe#getLeftOverItems(CraftingInventory)}
	 * @param hidden used in {@link ProxyRecipe#isHidden()}
	 * @param key used in {@link ProxyRecipe#getKey()}
	 */
	public ProxyRecipe(BiPredicate<? super CraftingInventory, ? super World> matcher,
			Function<? super CraftingInventory, ? extends ItemStack> crafter,
			Supplier<? extends ItemStack> result,
			Function<? super CraftingInventory, ? extends List<? extends ItemStack>> leftover,
			BooleanSupplier hidden,
			Supplier<NamespacedKey> key) {
		
		this.matcher = Objects.requireNonNull(matcher);
		this.crafter = Objects.requireNonNull(crafter);
		this.result = Objects.requireNonNull(result);
		this.leftover = Objects.requireNonNull(leftover);
		this.hidden = Objects.requireNonNull(hidden);
		this.key = Objects.requireNonNull(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		return matcher.test(craftingInventory, world);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack craftItem(CraftingInventory craftingInventory) {
		return crafter.apply(craftingInventory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack getResult() {
		return result.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
		return leftover.apply(craftingInventory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHidden() {
		return hidden.getAsBoolean();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NamespacedKey getKey() {
		return key.get();
	}

}
