package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.FireworksRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSFireworks;

import net.minecraft.server.v1_12_R1.RecipeFireworks;

public class CRFireworksRecipe extends CRCraftingRecipe<RecipeFireworks, NMSFireworks> implements FireworksRecipe {

	public CRFireworksRecipe(NMSFireworks nmsRecipe) {
		super(nmsRecipe);
	}

}
