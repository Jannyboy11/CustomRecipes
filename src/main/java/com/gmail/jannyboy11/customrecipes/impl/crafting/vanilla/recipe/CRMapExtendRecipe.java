package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.MapExtendRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSMapExtend;

import net.minecraft.server.v1_12_R1.RecipeMapExtend;

public class CRMapExtendRecipe extends CRCraftingRecipe<RecipeMapExtend, NMSMapExtend> implements MapExtendRecipe {
    //special case of a shaped recipe, that behaved differently, don't implement ShapedRecipe for the bukkit version.
    
	public CRMapExtendRecipe(NMSMapExtend nmsRecipe) {
		super(nmsRecipe);
	}

}
