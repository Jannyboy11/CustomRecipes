package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import java.util.function.Predicate;

import net.minecraft.server.v1_12_R1.ItemStack;

public class CombinedOrIngredient extends CombinedIngredient {

	public CombinedOrIngredient(Predicate<? super ItemStack> p1, Predicate<? super ItemStack> p2) {
		super(p1, p2, Boolean::logicalOr);
	}

}
