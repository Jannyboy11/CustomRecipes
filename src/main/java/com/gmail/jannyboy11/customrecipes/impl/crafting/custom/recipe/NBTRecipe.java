package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.CombinedIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class NBTRecipe extends ShapedRecipes {
	
	private final List<NBTTagCompound> ingredientTags;

	public NBTRecipe(String group, int width, int height, NonNullList<RecipeItemStack> ingredients,
			ItemStack result, List<NBTTagCompound> ingredientTags) {
		
		super(group, width, height, makeNBTSpecific(ingredients, ingredientTags), result);
		this.ingredientTags = ingredientTags;
	}
	
	private static NonNullList<RecipeItemStack> makeNBTSpecific(NonNullList<RecipeItemStack> ingredients, List<NBTTagCompound> to) {
		NonNullList<RecipeItemStack> result = NonNullList.a(ingredients.size(), RecipeItemStack.a);		
		for (int i = 0; i < ingredients.size(); i++) {
			RecipeItemStack originalIngredient = ingredients.get(i);
			NBTTagCompound compound = to.get(i);
			//TODO Make a wrapper type for the NBT ingredient when I'm including an NBT api
			CombinedIngredient combinedIngredient = new CombinedIngredient(originalIngredient,
					itemStack -> Objects.equals(itemStack.getTag(), compound),
					Boolean::logicalAnd);
			result.set(i, combinedIngredient.asNMSIngredient());
		}
		return result;
	}
	
	public List<NBTTagCompound> getIngredientTags() {
		return Collections.unmodifiableList(ingredientTags);
	}
	
}
