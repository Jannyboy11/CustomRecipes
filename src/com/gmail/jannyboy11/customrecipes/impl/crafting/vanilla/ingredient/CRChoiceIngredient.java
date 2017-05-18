package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;

import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRChoiceIngredient extends CRCraftingIngredient<RecipeItemStack> implements ChoiceIngredient {

	public CRChoiceIngredient(RecipeItemStack nmsIngredient) {
		super(nmsIngredient);
	}

	@Override
	public boolean isIngredient(ItemStack itemStack) {
		return this.nmsIngredient.test(CraftItemStack.asNMSCopy(itemStack));
	}

	@Override
	public List<? extends ItemStack> getChoices() {
		return Arrays.stream(nmsIngredient.choices).map(CraftItemStack::asCraftMirror).collect(Collectors.toList());
	}

}
