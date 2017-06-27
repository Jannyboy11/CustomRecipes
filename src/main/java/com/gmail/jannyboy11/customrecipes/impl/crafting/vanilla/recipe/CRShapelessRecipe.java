package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CRShapelessRecipe<R extends ShapelessRecipes> extends CRVanillaRecipe<R> implements ShapelessRecipe {

	public CRShapelessRecipe(R nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRShapelessRecipe(NBTTagCompound recipeCompound) {
		this((R) deserializeNmsRecipe(recipeCompound));
	}
	
	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound serialized = super.serialize();
		NBTTagList ingredients = new NBTTagList();
		for (RecipeItemStack ingr : nmsIngredients()) {
			ingredients.add(NBTUtil.serializeRecipeItemStack(ingr));
		}
		serialized.set("ingredients", ingredients);
		return serialized;
	}
	
	protected static ShapelessRecipes deserializeNmsRecipe(NBTTagCompound recipeCompound) {
		String group = recipeCompound.hasKeyOfType("group", 8 /*8 = string*/) ? recipeCompound.getString("group") : "";
		NonNullList<RecipeItemStack> ingredients = NonNullList.a();
		NBTTagList nbtIngredients = recipeCompound.getList("ingredients", 10 /*10 = compound*/);
		for (int i = 0; i < nbtIngredients.size(); i++) {
			NBTTagCompound ingredientTag = nbtIngredients.get(i);
			RecipeItemStack recipeItemStack = NBTUtil.deserializeRecipeItemStack(ingredientTag);
			ingredients.add(recipeItemStack);
		}
		NBTTagCompound resultCompound = (NBTTagCompound) recipeCompound.get("result");
		ItemStack result = new ItemStack(resultCompound);
		return new ShapelessRecipes(group, result, ingredients);
	}
	
	@Override
	public List<CRChoiceIngredient> getIngredients() {
		return nmsIngredients().stream().map(CRCraftingIngredient::getVanilla).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	private NonNullList<RecipeItemStack> nmsIngredients() {
		return (NonNullList<RecipeItemStack>) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "ingredients");
	}
	
	@Override
	public String getGroup() {
		return (String) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "c");
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapelessRecipe)) return false;
		if (o instanceof CRShapelessRecipe) return Objects.equals(this.nmsRecipe, ((CRShapelessRecipe<?>) o).nmsRecipe);
		
		ShapelessRecipe that = (ShapelessRecipe) o;
		
		return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getIngredients(), that.getIngredients()) &&
				Objects.equals(this.isHidden(), that.isHidden()) && Objects.equals(this.getGroup(), that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getResult(), getIngredients(), isHidden(), getGroup());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"result()=" + getResult() +
			",ingredients()=" + getIngredients() +
			",hidden()=" + isHidden() +
			",group()=" + getGroup() +				
			"}";
	}

}
