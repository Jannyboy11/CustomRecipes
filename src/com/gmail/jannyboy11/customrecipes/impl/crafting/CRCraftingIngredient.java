package com.gmail.jannyboy11.customrecipes.impl.crafting;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient.Bukkit2NMSIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CREmptyIngredient;

import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRCraftingIngredient<I extends RecipeItemStack> implements CraftingIngredient {

	protected final I nmsIngredient;

	public CRCraftingIngredient(I nmsIngredient) {
		this.nmsIngredient = nmsIngredient;
	}

	public static CRChoiceIngredient getVanilla(RecipeItemStack ingredient) {
		return ingredient == RecipeItemStack.a ? CREmptyIngredient.INSTANCE : new CRChoiceIngredient(ingredient);
	}

	@Override
	public boolean isIngredient(ItemStack itemStack) {
		return this.nmsIngredient.test(CraftItemStack.asNMSCopy(itemStack));
	}

	public static RecipeItemStack asNMSCopy(CraftingIngredient ingredient) {
		return new Bukkit2NMSIngredient(ingredient).asNMSIngredient();
	}

}
