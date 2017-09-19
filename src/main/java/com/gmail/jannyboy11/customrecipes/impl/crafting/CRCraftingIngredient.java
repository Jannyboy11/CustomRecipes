package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.Bukkit2NMSIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CREmptyIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRCraftingIngredient<I extends RecipeItemStack> implements Ingredient, NBTSerializable {

	protected final I nmsIngredient;

	public CRCraftingIngredient(I nmsIngredient) {
		this.nmsIngredient = nmsIngredient;
	}

	@Override
	public boolean isIngredient(ItemStack itemStack) {
		return this.nmsIngredient.a(CraftItemStack.asNMSCopy(itemStack));
	}

	@Override
	public NBTTagCompound serializeToNbt() {
		return NBTUtil.serializeRecipeItemStack(nmsIngredient);
	}
	
	@Override
	public Map<String, Object> serialize() {
		return NBTSerializable.super.serialize();
	}

	public static CRChoiceIngredient asBukkitIngredient(RecipeItemStack ingredient) {
	    //TODO can we handle subclasses of RecipeItemStack?
		return ingredient == RecipeItemStack.a ? CREmptyIngredient.INSTANCE : new CRChoiceIngredient(ingredient);
	}
	
	public static RecipeItemStack asNMSIngredient(ChoiceIngredient ingredient) {
		if (ingredient instanceof CRChoiceIngredient) {
			CRChoiceIngredient crIngredient = (CRChoiceIngredient) ingredient;
			return crIngredient.nmsIngredient;
		}
		
		RecipeItemStack nmsIngredient = asNMSIngredient((Ingredient) ingredient);
		ItemStack[] choices = ingredient.getChoices().stream().map(CraftItemStack::asNMSCopy).toArray(size -> new ItemStack[size]);
		ReflectionUtil.setFinalFieldValue(nmsIngredient, "choices", choices);
		
		return nmsIngredient;
	}

	public static RecipeItemStack asNMSIngredient(Ingredient ingredient) {
		if (ingredient instanceof CRCraftingIngredient) {
			@SuppressWarnings("unchecked")
			CRCraftingIngredient<RecipeItemStack> crIngredient = (CRCraftingIngredient<RecipeItemStack>) ingredient;
			return crIngredient.nmsIngredient;
		}
		
		//fallback
		return new Bukkit2NMSIngredient(ingredient).asNMSIngredient();
	}

}
