package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.CraftingInventory;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.WorldServer;

//TODO override getRepresentation in all subclasses
public class CRCraftingRecipe<R extends IRecipe> implements CraftingRecipe {
	
	protected final R nmsRecipe;
	
	protected CRCraftingRecipe(R nmsRecipe) {		
		this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
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
		return nmsRecipe.c();
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
	
	/**
	 * Get the key of this recipe
	 * 
	 * @return the key if this recipe is registered, otherwise null
	 */
	public final NamespacedKey getKey() {
		CRCraftingManager craftingManager = CustomRecipesPlugin.getInstance().getCraftingManager();
		MinecraftKey mcKey = craftingManager.getKey(nmsRecipe);
		return mcKey == null ? null : CraftNamespacedKey.fromMinecraft(mcKey);
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
	
	@Override
	public String toString() {
		return getClass().getName() + "{" +
				"key()=" + getKey() +
				",result()=" + InventoryUtils.getItemName(getResult()) +
				",hidden()=" + isHidden() +
				"}";
	}
	
}
