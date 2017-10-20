package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.MapCloneRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSMapClone;

import net.minecraft.server.v1_12_R1.RecipeMapClone;

public class CRMapCloneRecipe extends CRCraftingRecipe<RecipeMapClone, NMSMapClone> implements MapCloneRecipe {

	public CRMapCloneRecipe(NMSMapClone nmsRecipe) {
		super(nmsRecipe);
	}

}
