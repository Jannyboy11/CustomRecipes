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
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.ShapedRecipes;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;
import net.minecraft.server.v1_12_R1.WorldServer;

public class CRCraftingRecipe<R extends IRecipe> implements CraftingRecipe {
	
	//dirty hacks!
	private static final String CUSTOM_RECIPES_BUKKITRECIPE_KEY = "cr-craftingrecipe-";
	private static final AtomicInteger keyCount = new AtomicInteger(0);
	
	public static NamespacedKey newNamespacedKey() {
		CustomRecipesPlugin plugin = CustomRecipesPlugin.getInstance();
		return new NamespacedKey(plugin, CUSTOM_RECIPES_BUKKITRECIPE_KEY + keyCount.getAndIncrement() + UUID.randomUUID().toString());
	}
	
	
	protected final R nmsRecipe;
	protected MinecraftKey key; //this is a dirty hack that Spigot uses 
	
	@Deprecated
	protected CRCraftingRecipe(R nmsRecipe) {		
		this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
		this.key = nmsRecipe instanceof ShapedRecipes ? ((ShapedRecipes) nmsRecipe).key :
			nmsRecipe instanceof ShapelessRecipes ? ((ShapelessRecipes) nmsRecipe).key :
			nmsRecipe instanceof Bukkit2NMSRecipe ? ((Bukkit2NMSRecipe) nmsRecipe).getKey() : null;
		
		if (this.key == null) {
			CustomRecipesPlugin plugin = CustomRecipesPlugin.getInstance();
			plugin.getLogger().warning("Some NMS plugin used a custom crafting recipe implementation without providing a key!");
		}
	}
	
	public CRCraftingRecipe(R nmsRecipe, MinecraftKey key) {
		this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
		this.key = Objects.requireNonNull(key);
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
	
	@Override
	public NamespacedKey getKey() {
		if (key == null) key = CraftNamespacedKey.toMinecraft(newNamespacedKey());
		return CraftNamespacedKey.fromMinecraft(key);
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
				"key=" + key +
				",result()=" + InventoryUtils.getItemName(getResult()) +
				",hidden()=" + isHidden() +
				"}";
	}
	
}
