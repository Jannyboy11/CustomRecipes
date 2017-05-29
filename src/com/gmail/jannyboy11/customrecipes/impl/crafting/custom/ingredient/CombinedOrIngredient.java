package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import java.util.function.Predicate;

import net.minecraft.server.v1_12_R1.ItemStack;

public class CombinedOrIngredient extends InjectedIngredient {

	public CombinedOrIngredient(Predicate<? super ItemStack> p1, Predicate<? super ItemStack> p2) {
		super(itemStack -> p1.test(itemStack) || p2.test(itemStack));
	}

}
