package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;

public class SimpleChoiceIngredient implements ChoiceIngredient {
	
	public static final ChoiceIngredient ACCEPTING_EMPTY = new ChoiceIngredient() {
		@Override
		public boolean isIngredient(ItemStack itemStack) {
			return InventoryUtils.isEmptyStack(itemStack);
		}
		@Override
		public List<? extends ItemStack> getChoices() {
			return Collections.emptyList();
		}
	};
	
	private final List<? extends ItemStack> choices;
	
	public SimpleChoiceIngredient(List<? extends ItemStack> choices) {
		this.choices = Objects.requireNonNull(choices);
	}
	
	public static ChoiceIngredient fromChoices(ItemStack... choices) {
		if (choices == null || choices.length == 0) return ACCEPTING_EMPTY;
		return new SimpleChoiceIngredient(Arrays.asList(choices));
	}

	@Override
	public boolean isIngredient(ItemStack itemStack) {
		return choices.stream().anyMatch(choice -> choice == null ? itemStack == null : choice.getData().equals(itemStack.getData()));
	}

	@Override
	public List<? extends ItemStack> getChoices() {
		return choices;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ChoiceIngredient)) return false;
		ChoiceIngredient that = (ChoiceIngredient) o;
		return Objects.equals(this.getChoices(), that.getChoices());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getChoices());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" +
				"choices=" + choices +
				"}";
	}

}
