package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.NBTRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;

public class CRNBTRecipe extends CRShapedRecipe<NBTRecipe> implements com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe.NBTRecipe {

	public CRNBTRecipe(NBTRecipe nbtRecipe) {
		super(nbtRecipe);
	}

	//TODO add method to obtain List<Compound>
	//TODO I could use an existing NBT library such as JNBT for this
	//TODO I may want do use this anyway to allow for proper serialization and deserialization.

}
