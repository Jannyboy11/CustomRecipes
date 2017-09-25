package com.gmail.jannyboy11.customrecipes.impl.ingredient;

import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;

public class Bukkit2NMSIngredient extends InjectedIngredient {
	
	private final Ingredient bukkitIngredient;

	public Bukkit2NMSIngredient(Ingredient bukkitIngredient) {
		super (nmsStack -> bukkitIngredient.test(CraftItemStack.asCraftMirror(nmsStack)));
		this.bukkitIngredient = bukkitIngredient;
	}
	
	public static Bukkit2NMSIngredient deserialize(Map<String, Object> map) {
		Ingredient bukkitIngredient = (Ingredient) map.get("bukkitIngredient");
		return new Bukkit2NMSIngredient(bukkitIngredient);
	}

}
