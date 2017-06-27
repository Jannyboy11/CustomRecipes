package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.BookCloneRecipe;

import net.minecraft.server.v1_12_R1.RecipeBookClone;

public class CRBookCloneRecipe extends CRShapelessRecipe<RecipeBookClone> implements BookCloneRecipe {

	public CRBookCloneRecipe(RecipeBookClone nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRBookCloneRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
