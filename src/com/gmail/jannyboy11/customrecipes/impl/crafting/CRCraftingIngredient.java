package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.function.Predicate;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient.CREmptyIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRCraftingIngredient<I extends RecipeItemStack> {
	
	protected final I nmsIngredient;
	
	public CRCraftingIngredient(I nmsIngredient) {
		this.nmsIngredient = nmsIngredient;
	}

	public static CRChoiceIngredient getVanilla(RecipeItemStack ingredient) {
		return ingredient == RecipeItemStack.a ? CREmptyIngredient.INSTANCE : new CRChoiceIngredient(ingredient);
	}
	
	public static RecipeItemStack asNMSPredicate(Predicate<ItemStack> predicate) {
		return new RecipeItemStack() {
			@Override
			public boolean a(ItemStack nmsStack) {
				return predicate.test(nmsStack);
			}
		};
	}
	
	public static RecipeItemStack asNMSCopy(Predicate<org.bukkit.inventory.ItemStack> predicate) {
		return asNMSPredicate(nmsStack -> predicate.test(CraftItemStack.asCraftMirror(nmsStack)));
	}
	
}
