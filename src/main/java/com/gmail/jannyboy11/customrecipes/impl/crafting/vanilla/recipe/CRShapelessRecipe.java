package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CRShapelessRecipe<V extends ShapelessRecipes, S extends NMSShapelessRecipe<V>> extends CRCraftingRecipe<V, S> implements ShapelessRecipe {

	public CRShapelessRecipe(S nmsRecipe) {
		super(nmsRecipe);
	}
	
	public CRShapelessRecipe(NBTTagCompound recipeCompound) {
		this((S) new NMSShapelessRecipe((V) deserializeNmsRecipe(recipeCompound)));
	}
	
	public CRShapelessRecipe(Map<String, ?> map) {
		this(NBTUtil.fromMap(map));
	}

	//TODO refactor this to NMSShapelessRecipes?
	public NBTTagCompound serializeToNbt() {
		NBTTagCompound serialized = new NBTTagCompound();
		serialized.set("key", NBTUtil.serializeKey(getHandle().getKey()));
		serialized.set("result", NBTUtil.serializeItemStack(getHandle().b()));
		if (hasGroup()) serialized.setString("group", getGroup());
		NBTTagList ingredients = new NBTTagList();
		for (RecipeItemStack ingr : nmsIngredients()) {
			ingredients.add(NBTUtil.serializeRecipeItemStack(ingr));
		}
		serialized.set("ingredients", ingredients);
		return serialized;
	}

	//TODO refactor this method to NMSCraftingRecipe / NMSShapelessRecipes?
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
	public String getGroup() {
		return (String) ReflectionUtil.getDeclaredFieldValue(nmsRecipe, "c");
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapelessRecipe)) return false;
		if (o instanceof CRShapelessRecipe) return Objects.equals(this.nmsRecipe, ((CRShapelessRecipe<?, ?>) o).nmsRecipe);
		
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
