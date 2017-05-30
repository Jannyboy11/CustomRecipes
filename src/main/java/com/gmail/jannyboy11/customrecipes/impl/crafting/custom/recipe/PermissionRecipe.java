package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.World;

public class PermissionRecipe extends ShapedRecipes {

	private final String permission;
	
	public PermissionRecipe(String group, int width, int height, NonNullList<RecipeItemStack> ingredients, ItemStack result, String permission) {
		super(group, width, height, ingredients, result);
		this.permission = permission;
	}
	
	@Override
	public boolean a(InventoryCrafting inventoryCrafting, World world) {
		if (inventoryCrafting.getOwner() instanceof Player) {
			if (!((Player) inventoryCrafting.getOwner()).hasPermission(permission)) return false;
		}
		return super.a(inventoryCrafting, world);
	}
	
	public String getPermission() {
		return permission;
	}

}
