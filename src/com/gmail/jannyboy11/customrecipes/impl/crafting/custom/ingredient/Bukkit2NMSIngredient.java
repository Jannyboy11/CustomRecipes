package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.ingredient;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingIngredient;

public class Bukkit2NMSIngredient extends InjectedIngredient {

	public Bukkit2NMSIngredient(CraftingIngredient bukkitIngredient) {
		super (nmsStack -> bukkitIngredient.test(CraftItemStack.asCraftMirror(nmsStack)));
	}

}
