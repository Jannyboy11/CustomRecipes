package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.TippedArrowRecipe;

import net.minecraft.server.v1_12_R1.RecipeTippedArrow;

public class CRTippedArrowRecipe extends CRShapedRecipe<RecipeTippedArrow> implements TippedArrowRecipe {

	public CRTippedArrowRecipe(RecipeTippedArrow nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRTippedArrowRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
