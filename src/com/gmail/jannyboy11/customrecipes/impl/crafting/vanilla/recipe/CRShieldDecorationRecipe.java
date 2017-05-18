package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShieldDecorationRecipe;

import net.minecraft.server.v1_12_R1.RecipiesShield;
import net.minecraft.server.v1_12_R1.RecipiesShield.Decoration;

public class CRShieldDecorationRecipe extends CRShapelessRecipe<RecipiesShield.Decoration> implements ShieldDecorationRecipe {

	public CRShieldDecorationRecipe(Decoration nmsRecipe) {
		super(nmsRecipe);
	}

}
