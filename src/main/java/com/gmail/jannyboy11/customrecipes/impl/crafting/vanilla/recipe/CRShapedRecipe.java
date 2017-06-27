package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
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

public class CRShapedRecipe<R extends ShapedRecipes> extends CRVanillaRecipe<R> implements ShapedRecipe {

	public CRShapedRecipe(R nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRShapedRecipe(NBTTagCompound recipeCompound) {
		this((R) deserializeNmsRecipe(recipeCompound));
	}
	
	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound serialized = super.serialize();
		serialized.setInt("width", getWidth());
		serialized.setInt("height", getHeight());
		NBTTagList ingredients = new NBTTagList();
		for (RecipeItemStack ingr : nmsIngredients()) {
			ingredients.add(NBTUtil.serializeRecipeItemStack(ingr));
		}
		serialized.set("ingredients", ingredients);
		return serialized;
	}
	
	protected static ShapedRecipes deserializeNmsRecipe(NBTTagCompound recipeCompound) {
		String group = recipeCompound.hasKeyOfType("group", 8 /*8 = string*/) ? recipeCompound.getString("group") : "";
		int width = recipeCompound.getInt("width");
		int height = recipeCompound.getInt("height");
		NonNullList<RecipeItemStack> ingredients = NonNullList.a();
		NBTTagList nbtIngredients = recipeCompound.getList("ingredients", 10 /*10 = compound*/);
		for (int i = 0; i < nbtIngredients.size(); i++) {
			NBTTagCompound ingredientTag = nbtIngredients.get(i);
			RecipeItemStack recipeItemStack = NBTUtil.deserializeRecipeItemStack(ingredientTag);
			ingredients.add(recipeItemStack);
		}
		NBTTagCompound resultCompound = (NBTTagCompound) recipeCompound.get("result");
		ItemStack result = new ItemStack(resultCompound);
		return new ShapedRecipes(group, width, height, ingredients, result);
	}
	
	@Override
	public int getWidth() {
		return (int) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "width");
	}
	
	@Override
	public int getHeight() {
		return (int) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "height");
	}
	
	@Override
	public List<CRChoiceIngredient> getIngredients() {
		return nmsIngredients().stream().map(CRCraftingIngredient::getVanilla).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	protected NonNullList<RecipeItemStack> nmsIngredients() {
		return (NonNullList<RecipeItemStack>) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "items");
	}

	@Override
	public String getGroup() {
		return (String) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "e");
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapedRecipe)) return false;
		if (o instanceof CRShapedRecipe) return Objects.equals(this.nmsRecipe, ((CRShapedRecipe<?>) o).nmsRecipe);
		
		ShapedRecipe that = (ShapedRecipe) o;
		
		return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getIngredients(), that.getIngredients()) &&
				Objects.equals(this.getWidth(), that.getWidth()) && Objects.equals(this.getHeight(), that.getHeight()) &&
				Objects.equals(this.isHidden(), that.isHidden()) && Objects.equals(this.getGroup(), that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getResult(), getWidth(), getHeight(), getIngredients(), isHidden(), getGroup());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"result()=" + getResult() +
			",width()=" + getWidth() +
			",heigth()=" + getHeight() +
			",ingredients()=" + getIngredients() +
			",hidden()=" + isHidden() +
			",group()=" + getGroup() +				
			"}";
	}

}
