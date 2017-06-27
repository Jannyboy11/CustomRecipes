package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.CraftingManager;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.WorldServer;

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
		MinecraftKey mcKey = getMinecraftKey();
		return mcKey == null ? null : CraftNamespacedKey.fromMinecraft(mcKey);
	}
	
	private MinecraftKey getMinecraftKey() {
		return CraftingManager.recipes.b(nmsRecipe);
	}
	
	@Override
	public ItemStack getRepresentation() {
		ItemStack result = getResult();
		
		ItemStack representation = (result == null || result.getType() == Material.AIR) ? new ItemStack(Material.AIR) : result.clone();
		if (representation.getType() == Material.AIR) {
			representation = new ItemStack(Material.STRUCTURE_BLOCK);
			ItemMeta meta = representation.getItemMeta();
			meta.setDisplayName(InventoryUtils.getItemName(getResult()));
			meta.setLore(Arrays.asList("Result: UNKNOWN", ChatColor.DARK_GRAY + "Key: " + getMinecraftKey()));
			representation.setItemMeta(meta);
			return representation;
		}
		
		ItemMeta meta = representation.getItemMeta();

		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_GRAY + "Hidden: " + isHidden());
		lore.add(ChatColor.DARK_GRAY + "Key: " + getMinecraftKey());
		
		meta.setDisplayName(ChatColor.GRAY + InventoryUtils.getItemName(getResult()));
		meta.setLore(lore);
		
		representation.setItemMeta(meta);
		return representation;
	}
	
	public NBTTagCompound serialize() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.set("result", nmsRecipe.b().save(new NBTTagCompound()));
		if (hasGroup()) compound.setString("group", getGroup());
		MinecraftKey key = getMinecraftKey();
		if (key != null) compound.set("key", NBTUtil.serializeKey(key));
		return compound;
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
