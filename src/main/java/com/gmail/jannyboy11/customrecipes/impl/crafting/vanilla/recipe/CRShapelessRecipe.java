package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CRShapelessRecipe<R extends ShapelessRecipes> extends CRVanillaRecipe<R> implements ShapelessRecipe {

	public CRShapelessRecipe(R nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRShapelessRecipe(NBTTagCompound recipeCompound) {
		this((R) deserializeNmsRecipe(recipeCompound));
	}
	
	public CRShapelessRecipe(Map<String, ?> map) {
		this(NBTUtil.fromMap(map));
	}
	
	@Override
	public NBTTagCompound serializeToNbt() {
		NBTTagCompound serialized = super.serializeToNbt();
		NBTTagList ingredients = new NBTTagList();
		for (RecipeItemStack ingr : nmsIngredients()) {
			ingredients.add(NBTUtil.serializeRecipeItemStack(ingr));
		}
		serialized.set("ingredients", ingredients);
		return serialized;
	}
	
	protected static ShapelessRecipes deserializeNmsRecipe(NBTTagCompound recipeCompound) {
		String group = recipeCompound.hasKeyOfType("group", NBTUtil.STRING) ? recipeCompound.getString("group") : "";
		NonNullList<RecipeItemStack> ingredients = NonNullList.a();
		NBTTagList nbtIngredients = recipeCompound.getList("ingredients", NBTUtil.COMPOUND);
		for (int i = 0; i < nbtIngredients.size(); i++) {
			NBTTagCompound ingredientTag = nbtIngredients.get(i);
			RecipeItemStack recipeItemStack = NBTUtil.deserializeRecipeItemStack(ingredientTag);
			ingredients.add(recipeItemStack);
		}
		NBTTagCompound resultCompound = (NBTTagCompound) recipeCompound.get("result");
		ItemStack result = new ItemStack(resultCompound);
		ShapelessRecipes shapelessRecipes = new ShapelessRecipes(group, result, ingredients);
		if (recipeCompound.hasKey("key")) {
			shapelessRecipes.setKey(NBTUtil.deserializeKey(recipeCompound.getCompound("key")));
		}
		return shapelessRecipes;
	}
	
	@Override
	public List<CRChoiceIngredient> getIngredients() {
		return nmsIngredients().stream().map(CRCraftingIngredient::asBukkitIngredient).collect(Collectors.toList());
	}
	
	@SuppressWarnings("unchecked")
	private NonNullList<RecipeItemStack> nmsIngredients() {
		return (NonNullList<RecipeItemStack>) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "ingredients");
	}
	
	@Override
	public MinecraftKey getMinecraftKey() {
		return nmsRecipe.key;
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
