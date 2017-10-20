package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.TippedArrowRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSTippedArrow;

import net.minecraft.server.v1_12_R1.RecipeTippedArrow;

public class CRTippedArrowRecipe extends CRCraftingRecipe<RecipeTippedArrow, NMSTippedArrow> implements TippedArrowRecipe {

	public CRTippedArrowRecipe(NMSTippedArrow nmsRecipe) {
		super(nmsRecipe);
	}

}
