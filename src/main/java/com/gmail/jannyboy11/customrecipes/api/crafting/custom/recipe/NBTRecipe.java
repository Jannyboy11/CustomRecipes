package com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;

/**
 * Represents an NBT recipe
 * 
 * @author Jan
 */
public interface NBTRecipe extends ShapedRecipe {
	
	//TODO add method to obtain List<Compound> for the ingredients
	//TODO I could use an existing NBT library such as JNBT for this
	//TODO in that case, also override getRepresentation

}
