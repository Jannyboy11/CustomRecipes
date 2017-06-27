package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingManager;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.World;

public class Bukkit2NMSRecipe implements IRecipe, NBTSerializable {
	
	//dirty hacks!
	private static final String CUSTOM_RECIPES_BUKKITRECIPE_KEY = "cr-bukkit2nmsrecipe-";
	private static final AtomicInteger keyCount = new AtomicInteger(0);
	
	private static NamespacedKey newNamespacedKey() {
		CustomRecipesPlugin plugin = CustomRecipesPlugin.getInstance();
		return new NamespacedKey(plugin, CUSTOM_RECIPES_BUKKITRECIPE_KEY + keyCount.getAndIncrement() + UUID.randomUUID().toString());
	}
	
	
	private CraftingRecipe crRecipe;

	public Bukkit2NMSRecipe(CraftingRecipe bukkitRecipe) {
		this.crRecipe = Objects.requireNonNull(bukkitRecipe);
	}
	
	@Override
	public Map<String, Object> serialize() {
		return Collections.singletonMap("crRecipe", crRecipe);
	}
	
	@Override
	public NBTTagCompound serializeToNbt() {
		return NBTUtil.fromMap(serialize());
	}
	
	
	public static CraftInventoryCrafting getBukkitCraftingInventory(InventoryCrafting inventoryCrafting) {
		IInventory resultInventory = inventoryCrafting.resultInventory;
		return new CraftInventoryCrafting(inventoryCrafting, resultInventory);
	}

	@Override
	public boolean a(InventoryCrafting inventoryCrafting, World world) {
		CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);
		CraftWorld bukkitWorld = world.getWorld();		
		
		return crRecipe.matches(bukkitInventory, bukkitWorld);
	}

	@Override
	public ItemStack b() {
		return CraftItemStack.asNMSCopy(crRecipe.getResult());
	}

	@Override
	public NonNullList<ItemStack> b(InventoryCrafting inventoryCrafting) {
		CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);

		List<? extends org.bukkit.inventory.ItemStack> bukkitLeftovers = crRecipe.getLeftOverItems(bukkitInventory);
		ItemStack[] nmsLeftovers = bukkitLeftovers.stream().map(CraftItemStack::asNMSCopy).toArray(size -> new ItemStack[size]);
		return NonNullList.a(ItemStack.a, nmsLeftovers);
	}

	@Override
	public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
		CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);
		
		return CraftItemStack.asNMSCopy(crRecipe.craftItem(bukkitInventory));
	}
	
	@Override
	public boolean c() {
		return crRecipe.isHidden();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof Bukkit2NMSRecipe)) return false;
		Bukkit2NMSRecipe that = (Bukkit2NMSRecipe) object;
		return this.crRecipe.equals(that.crRecipe);
	}
	
	@Override
	public int hashCode() {
		return crRecipe.hashCode();
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public Recipe toBukkitRecipe() {
		//we try the best we can to do something meaningful.
		
		if (crRecipe instanceof CRShapedRecipe) {
			return ((CRShapedRecipe) crRecipe).getHandle().toBukkitRecipe();
			
		} else if (crRecipe instanceof CRShapelessRecipe) {
			return ((CRShapelessRecipe) crRecipe).getHandle().toBukkitRecipe();
			
			
		} else if (crRecipe instanceof ShapedRecipe) {
			//try our very best to do something useful
			ShapedRecipe shapedRecipe = (ShapedRecipe) crRecipe;
			NamespacedKey namespacedKey = CustomRecipesPlugin.getInstance().getCraftingManager().getKey(shapedRecipe);
			if (namespacedKey == null) namespacedKey = newNamespacedKey();
			
			org.bukkit.inventory.ShapedRecipe bukkitShapedRecipe = new org.bukkit.inventory.ShapedRecipe(namespacedKey, crRecipe.getResult());
			
			AtomicInteger character = new AtomicInteger('a');
			List<StringBuilder> shapeBuilder = new ArrayList<>();
			for (int i = 0; i < shapedRecipe.getHeight(); i++) {
				shapeBuilder.add(new StringBuilder());
				for (int j = 0; j < shapedRecipe.getWidth(); j++) {
					shapeBuilder.get(i).append((char) character.getAndIncrement());
				}
			}
			
			String[] shape = shapeBuilder.stream().map(StringBuilder::toString).toArray(size -> new String[size]);
			bukkitShapedRecipe.shape(shape);
			// for clarity. arrayNum goes from top to bottom, charNum goes from left to right
			/* [a,b,c],
			 * [d,e,f],
			 * [g,h,i]
			 */
			
			int arrayNum = 0;
			int charNum = 0;
			for (ChoiceIngredient ingr : shapedRecipe.getIngredients()) {
				Iterator<? extends org.bukkit.inventory.ItemStack> choiceIterator = ingr.getChoices().iterator();
				if (choiceIterator.hasNext()) {
					char key = shape[arrayNum].charAt(charNum);
					bukkitShapedRecipe.setIngredient(key, choiceIterator.next().getType());
				}
				//skip the other choices since they would take up the same item slot.
				//instead, the materialdata is ignored for this recipe, such that multiple data values would still work.
				//this seems like the most reasonable thing to do as we don't know what the choices are.
				
				if (++charNum > shape[arrayNum].length()) {
					charNum = 0;
					arrayNum++;
				}
			}
			
			return bukkitShapedRecipe;
			
		} else if (crRecipe instanceof ShapelessRecipe) {
			//try our very best to do something useful
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) crRecipe;
			NamespacedKey namespacedKey = CustomRecipesPlugin.getInstance().getCraftingManager().getKey(shapelessRecipe);
			if (namespacedKey == null) namespacedKey = newNamespacedKey();
			
			org.bukkit.inventory.ShapelessRecipe bukkitShapelessRecipe = new org.bukkit.inventory.ShapelessRecipe(namespacedKey, crRecipe.getResult());
			
			for (ChoiceIngredient ingr : shapelessRecipe.getIngredients()) {
				Iterator<? extends org.bukkit.inventory.ItemStack> choiceIterator = ingr.getChoices().iterator();
				if (choiceIterator.hasNext()) {
					bukkitShapelessRecipe.addIngredient(choiceIterator.next().getType());
				}
				//skip the other choices since they would take up the same item slot.
				//instead, the materialdata is ignored for this recipe, such that multiple data values would still work.
				//this seems like the most reasonable thing to do as we don't know what the choices are.
			}
			
			return bukkitShapelessRecipe;
		}
		
		//Recipe was neither a Shaped nor a Shapeless recipe, return the generic crafting recipe.
		//Bukkit and other plugins will not like this, but it's the best we can do instead of returning null.
		return crRecipe;
	}


	
	@Override
	public void setKey(MinecraftKey key) {		
		//no spigot pls. fuck you. the key is not part of the recipe itself dangit!
	}
	
	public MinecraftKey getKey() {
		CRCraftingManager craftingManager = CustomRecipesPlugin.getInstance().getCraftingManager();
		return craftingManager.getKey(this);
	}

}
