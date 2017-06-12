package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CRShapelessRecipe<R extends ShapelessRecipes> extends CRVanillaRecipe<R> implements ShapelessRecipe {

	public CRShapelessRecipe(R nmsRecipe) {
		super(nmsRecipe, nmsRecipe.key);
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
	public NamespacedKey getKey() {
		return CraftNamespacedKey.fromMinecraft(nmsRecipe.key);
	}
	
	@Override
	public String getGroup() {
		return (String) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "c");
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapelessRecipe)) return false;
		ShapelessRecipe that = (ShapelessRecipe) o;
		
		return Objects.equals(this.getKey(), that.getKey()) && Objects.equals(this.getResult(), that.getResult()) &&
				Objects.equals(this.getIngredients(), that.getIngredients()) && Objects.equals(this.isHidden(), that.isHidden()) &&
				Objects.equals(this.getGroup(), that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(key, getResult(), getIngredients(), isHidden(), getGroup());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"key=" + key +
			",result()=" + getResult() +
			",ingredients()=" + getIngredients() +
			",hidden()=" + isHidden() +
			",group()=" + getGroup() +				
			"}";
	}

}
