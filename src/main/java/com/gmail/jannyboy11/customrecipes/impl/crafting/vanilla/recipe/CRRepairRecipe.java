package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.RepairRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSRepair;

import net.minecraft.server.v1_12_R1.RecipeRepair;

public class CRRepairRecipe extends CRCraftingRecipe<RecipeRepair, NMSRepair> implements RepairRecipe {

	public CRRepairRecipe(NMSRepair nmsRecipe) {
		super(nmsRecipe);
	}

}
