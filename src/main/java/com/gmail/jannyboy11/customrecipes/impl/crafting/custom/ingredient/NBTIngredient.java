package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class NBTIngredient extends CombinedIngredient {
	
	private final RecipeItemStack basePredicate;

	public NBTIngredient(RecipeItemStack basePredicate, NBTTagCompound tag) {
		super(basePredicate, itemStack -> Objects.equals(itemStack.getTag(), tag), Boolean::logicalAnd);
		this.basePredicate = basePredicate;
	}

	@Override
	public RecipeItemStack asNMSIngredient() {
		RecipeItemStack recipeItemStack = super.asNMSIngredient();
		ReflectionUtil.setFinalFieldValue(recipeItemStack, "choices", basePredicate.choices);
		return recipeItemStack;
	}
	
}
