package com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla;

import java.util.Map;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.EmptyIngredient;

import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CREmptyIngredient extends CRChoiceIngredient implements EmptyIngredient {
	
	public static final CREmptyIngredient INSTANCE = new CREmptyIngredient();

	private CREmptyIngredient() {
		super(RecipeItemStack.a);
	}
	
	public static CREmptyIngredient deserialize(Map<String, Object> map) {
		return INSTANCE;
	}

}
