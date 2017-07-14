package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CountIngredient extends CombinedIngredient {
	
	private final RecipeItemStack basePredicate;
	private final int count;

	public CountIngredient(RecipeItemStack basePredicate, int count) {
		super(basePredicate, itemStack -> itemStack.getCount() == count, Boolean::logicalAnd);
		this.basePredicate = basePredicate;
		this.count = count;
	}
	
	@Override
	public RecipeItemStack asNMSIngredient() {
		RecipeItemStack recipeItemStack = super.asNMSIngredient();
		ReflectionUtil.setFinalFieldValue(recipeItemStack, "choices", basePredicate.choices);
		return recipeItemStack;
	}
	
	public int getCount() {
		return count;
	}

}
