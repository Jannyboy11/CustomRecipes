package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.RepairRecipe;

import net.minecraft.server.v1_12_R1.RecipeRepair;

public class CRRepairRecipe extends CRShapelessRecipe<RecipeRepair> implements RepairRecipe {

	public CRRepairRecipe(RecipeRepair nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRRepairRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
