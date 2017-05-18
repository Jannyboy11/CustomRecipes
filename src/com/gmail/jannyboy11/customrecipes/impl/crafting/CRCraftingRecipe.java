package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.CraftingInventory;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.WorldServer;

public class CRCraftingRecipe<R extends IRecipe> implements CraftingRecipe, Comparable<CRCraftingRecipe<?>> {
	
	protected final R nmsRecipe;
	
	public CRCraftingRecipe(R nmsRecipe) {
		this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
	}
	
	public boolean isSpecialRecipe() {
		return nmsRecipe.d();
	}

	@Override
	public int compareTo(CRCraftingRecipe<?> o) {
		return nmsRecipe.compareTo(o.nmsRecipe);
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		CraftWorld cWorld = (CraftWorld) world;
		
		WorldServer nmsWorld = cWorld.getHandle();
		InventoryCrafting nmsCraftingInventory = getNmsCraftingInventory(craftingInventory);

		return nmsRecipe.a(nmsCraftingInventory, nmsWorld);
	}

	@Override
	public CraftItemStack craftItem(CraftingInventory craftingInventory) {
		InventoryCrafting nmsCraftingInventory = getNmsCraftingInventory(craftingInventory);
		return CraftItemStack.asCraftMirror(nmsRecipe.craftItem(nmsCraftingInventory));
	}

	@Override
	public CraftItemStack getResult() {
		return CraftItemStack.asCraftMirror(nmsRecipe.b());
	}
	
	@Override
	public boolean isHidden() {
		return nmsRecipe.d();
	}

	@Override
	public List<CraftItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
		InventoryCrafting nmsCraftingInventory = getNmsCraftingInventory(craftingInventory);
		return nmsRecipe.b(nmsCraftingInventory).stream()
				.map(CraftItemStack::asCraftMirror)
				.collect(Collectors.toList());
	}
	
	public static InventoryCrafting getNmsCraftingInventory(CraftingInventory bukkitInventory) {
		return (InventoryCrafting) ReflectionUtil.getDeclaredFieldValue(bukkitInventory, "inventory");
	}
	
	public IRecipe getHandle() {
		return nmsRecipe;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof CRCraftingRecipe)) return false;
		@SuppressWarnings("rawtypes")
		CRCraftingRecipe that = (CRCraftingRecipe) object;
		return this.nmsRecipe.equals(that.nmsRecipe);
	}
	
	@Override
	public int hashCode() {
		return nmsRecipe.hashCode();
	}
}
