package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.MapExtendRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSMapExtend;

import net.minecraft.server.v1_12_R1.RecipeMapExtend;

public class CRMapExtendRecipe extends CRShapedRecipe<RecipeMapExtend, NMSMapExtend> implements MapExtendRecipe {

	public CRMapExtendRecipe(NMSMapExtend nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRMapExtendRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
