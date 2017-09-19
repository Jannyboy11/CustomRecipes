package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.BannerDuplicateRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSBannerDuplicate;

import net.minecraft.server.v1_12_R1.RecipesBanner;

public class CRBannerDuplicateRecipe extends CRShapelessRecipe<RecipesBanner.DuplicateRecipe, NMSBannerDuplicate> implements BannerDuplicateRecipe {

	public CRBannerDuplicateRecipe(NMSBannerDuplicate nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRBannerDuplicateRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
