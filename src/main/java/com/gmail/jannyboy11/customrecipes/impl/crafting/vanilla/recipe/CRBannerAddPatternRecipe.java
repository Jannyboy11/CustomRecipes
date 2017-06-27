package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.BannerAddPatternRecipe;

import net.minecraft.server.v1_12_R1.RecipesBanner;
import net.minecraft.server.v1_12_R1.RecipesBanner.AddRecipe;

public class CRBannerAddPatternRecipe extends CRShapelessRecipe<RecipesBanner.AddRecipe> implements BannerAddPatternRecipe {

	public CRBannerAddPatternRecipe(AddRecipe nmsRecipe) {
		super(nmsRecipe);
	}

	public CRBannerAddPatternRecipe(java.util.Map<String, ?> map) {
		super(map);
	}
}
