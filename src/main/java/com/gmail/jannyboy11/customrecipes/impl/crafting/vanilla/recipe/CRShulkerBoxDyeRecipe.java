package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShulkerBoxDyeRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSShulkerBoxDye;

import net.minecraft.server.v1_12_R1.RecipeShulkerBox;

public class CRShulkerBoxDyeRecipe extends CRCraftingRecipe<RecipeShulkerBox.Dye, NMSShulkerBoxDye> implements ShulkerBoxDyeRecipe {

	public CRShulkerBoxDyeRecipe(NMSShulkerBoxDye nmsRecipe) {
		super(nmsRecipe);
	}
	
}
