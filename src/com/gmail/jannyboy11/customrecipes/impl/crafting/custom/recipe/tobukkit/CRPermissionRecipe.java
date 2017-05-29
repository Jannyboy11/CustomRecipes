package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.PermissionRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;

public class CRPermissionRecipe extends CRShapedRecipe<PermissionRecipe> implements com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe.PermissionRecipe {

	public CRPermissionRecipe(PermissionRecipe nmsRecipe) {
		super(nmsRecipe);
	}

	@Override
	public String getPermission() {
		return nmsRecipe.getPermission();
	}

}
