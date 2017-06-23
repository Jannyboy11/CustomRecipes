package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;

/**
 * @deprecated internal use only, this class should neither be instantiated nor extended directly. It may get removed in future releases.
 */
@Deprecated
public class CRVanillaRecipe<R extends IRecipe> extends CRCraftingRecipe<R> implements CraftingRecipe {

	public CRVanillaRecipe(R nmsRecipe) {
		super(nmsRecipe);
	}

}
