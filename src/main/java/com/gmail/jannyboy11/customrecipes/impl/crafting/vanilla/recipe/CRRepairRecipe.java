package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.RepairRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSRepair;

import net.minecraft.server.v1_12_R1.RecipeRepair;

public class CRRepairRecipe extends CRShapelessRecipe<RecipeRepair, NMSRepair> implements RepairRecipe {

	public CRRepairRecipe(NMSRepair nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRRepairRecipe(java.util.Map<String, ?> map) {
		super(map);
	}

}
