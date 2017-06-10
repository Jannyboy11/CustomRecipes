package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import java.util.function.Predicate;

import com.gmail.jannyboy11.customrecipes.util.BooleanBinaryOpterator;

import net.minecraft.server.v1_12_R1.ItemStack;

public class CombinedIngredient extends InjectedIngredient {

	public CombinedIngredient(Predicate<? super ItemStack> p1, Predicate<? super ItemStack> p2, BooleanBinaryOpterator combiner) {
		super(itemStack -> combiner.applyAsBoolean(p1.test(itemStack), p2.test(itemStack)));
	}

}
