package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.tobukkit;

import java.util.Map;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.NBTRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRNBTRecipe extends CRShapedRecipe<NBTRecipe> implements com.gmail.jannyboy11.customrecipes.api.crafting.custom.recipe.NBTRecipe {

	public CRNBTRecipe(NBTRecipe nbtRecipe) {
		super(nbtRecipe);
	}

	public CRNBTRecipe(NBTTagCompound recipeCompound) {
		this(deserializeNmsRecipe(recipeCompound));
	}
	
	public CRNBTRecipe(Map<String, Object> map) {
		this(NBTUtil.fromMap(map));
	}
	
	protected static NBTRecipe deserializeNmsRecipe(NBTTagCompound recipeCompound) {
		String group = recipeCompound.hasKeyOfType("group", NBTUtil.STRING) ? recipeCompound.getString("group") : "";
		int width = recipeCompound.getInt("width");
		int height = recipeCompound.getInt("height");
		NonNullList<RecipeItemStack> ingredients = NonNullList.a();
		NBTTagList nbtIngredients = recipeCompound.getList("ingredients", NBTUtil.COMPOUND);
		for (int i = 0; i < nbtIngredients.size(); i++) {
			NBTTagCompound ingredientTag = nbtIngredients.get(i);
			RecipeItemStack recipeItemStack = NBTUtil.deserializeRecipeItemStack(ingredientTag);
			ingredients.add(recipeItemStack);
		}
		NBTTagCompound resultCompound = (NBTTagCompound) recipeCompound.get("result");
		ItemStack result = new ItemStack(resultCompound);
		//ingredients are made nbt specific in NBTRecipe constructor
		NBTRecipe nbtRecipe = new NBTRecipe(group, width, height, ingredients, result);
		if (recipeCompound.hasKey("key")) {
			nbtRecipe.setKey(NBTUtil.deserializeKey(recipeCompound.getCompound("key")));
		}
		return nbtRecipe;
	}
	
}
