package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.BookCloneRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSBookClone;

import net.minecraft.server.v1_12_R1.RecipeBookClone;

public class CRBookCloneRecipe extends CRShapelessRecipe<RecipeBookClone, NMSBookClone> implements BookCloneRecipe {

	public CRBookCloneRecipe(NMSBookClone nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRBookCloneRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
