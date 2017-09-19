package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.BannerAddPatternRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSBannerAdd;

import net.minecraft.server.v1_12_R1.RecipesBanner;

public class CRBannerAddPatternRecipe extends CRShapelessRecipe<RecipesBanner.AddRecipe, NMSBannerAdd> implements BannerAddPatternRecipe {

	public CRBannerAddPatternRecipe(NMSBannerAdd nmsRecipe) {
		super(nmsRecipe);
	}

	public CRBannerAddPatternRecipe(java.util.Map<String, ?> map) {
		super(map);
	}
}
