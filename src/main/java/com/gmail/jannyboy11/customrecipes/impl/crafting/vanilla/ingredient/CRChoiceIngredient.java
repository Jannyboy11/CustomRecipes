package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRChoiceIngredient extends CRCraftingIngredient<RecipeItemStack> implements ChoiceIngredient, NBTSerializable {

	public CRChoiceIngredient(RecipeItemStack nmsIngredient) {
		super(nmsIngredient);
	}
	
	public static CRChoiceIngredient deserialize(Map<String, Object> map) {
		RecipeItemStack nmsIngredient = NBTUtil.deserializeRecipeItemStack(NBTUtil.fromMap(map));
		return nmsIngredient == RecipeItemStack.a ? CREmptyIngredient.INSTANCE : new CRChoiceIngredient(nmsIngredient);
	}

	@Override
	public List<? extends ItemStack> getChoices() {
		return Arrays.stream(nmsIngredient.choices).map(CraftItemStack::asCraftMirror).collect(Collectors.toList());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o instanceof CRChoiceIngredient) {
			CRChoiceIngredient that = (CRChoiceIngredient) o;
			net.minecraft.server.v1_12_R1.ItemStack[] thisChoices = nmsIngredient.choices;
			net.minecraft.server.v1_12_R1.ItemStack[] thatChoices = that.nmsIngredient.choices;
			
			if (thisChoices.length != thatChoices.length) return false;
			
			for (int i = 0; i < thisChoices.length; i++) {
				net.minecraft.server.v1_12_R1.ItemStack thisChoice = thisChoices[i];
				net.minecraft.server.v1_12_R1.ItemStack thatChoice = thatChoices[i];
				if (!net.minecraft.server.v1_12_R1.ItemStack.fastMatches(thisChoice, thatChoice)) return false;
			}
			
			return true;
			
		} else if (o instanceof ChoiceIngredient) {
			return Objects.deepEquals(this.getChoices(), ((ChoiceIngredient) o).getChoices());
			
		}
	
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getChoices());
	}

}
