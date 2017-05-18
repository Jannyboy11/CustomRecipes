package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShulkerBoxDyeRecipe;

import net.minecraft.server.v1_12_R1.RecipeShulkerBox;
import net.minecraft.server.v1_12_R1.RecipeShulkerBox.Dye;

public class CRShulkerBoxDyeRecipe extends CRShapelessRecipe<RecipeShulkerBox.Dye> implements ShulkerBoxDyeRecipe {

	public CRShulkerBoxDyeRecipe(Dye nmsRecipe) {
		super(nmsRecipe);
	}

}
