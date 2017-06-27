package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.FireworksRecipe;

import net.minecraft.server.v1_12_R1.RecipeFireworks;

public class CRFireworksRecipe extends CRShapelessRecipe<RecipeFireworks> implements FireworksRecipe {

	public CRFireworksRecipe(RecipeFireworks nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRFireworksRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
