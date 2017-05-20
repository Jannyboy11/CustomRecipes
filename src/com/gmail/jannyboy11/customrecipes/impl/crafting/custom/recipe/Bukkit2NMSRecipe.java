package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShapelessRecipe;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.World;

public class Bukkit2NMSRecipe extends IRecipe {
	
	//dirty hacks!
	private static final String CUSTOM_RECIPES_BUKKITRECIPE_KEY = "customrecipes-bukkitrecipe-";
	private static final AtomicInteger keyCount = new AtomicInteger(0);
	
	private final CraftingRecipe bukkitRecipe;

	public Bukkit2NMSRecipe(CraftingRecipe bukkitRecipe) {
		this.bukkitRecipe = Objects.requireNonNull(bukkitRecipe);
	}
	
	public static CraftInventoryCrafting getBukkitCraftingInventory(InventoryCrafting inventoryCrafting) {
		IInventory resultInventory = inventoryCrafting.resultInventory;
		return new CraftInventoryCrafting(inventoryCrafting, resultInventory);
	}

	@Override
	public boolean a(InventoryCrafting inventoryCrafting, World world) {
		CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);
		CraftWorld bukkitWorld = world.getWorld();		
		
		return bukkitRecipe.matches(bukkitInventory, bukkitWorld);
	}

	@Override
	public ItemStack b() {
		return CraftItemStack.asNMSCopy(bukkitRecipe.getResult());
	}

	@Override
	public NonNullList<ItemStack> b(InventoryCrafting inventoryCrafting) {
		CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);

		List<? extends org.bukkit.inventory.ItemStack> bukkitLeftovers = bukkitRecipe.getLeftOverItems(bukkitInventory);
		
		ItemStack[] nmsLeftovers = bukkitLeftovers.toArray(new ItemStack[bukkitLeftovers.size()]);
		return NonNullList.a(ItemStack.a, nmsLeftovers);
	}

	@Override
	public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
		CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);
		
		return CraftItemStack.asNMSCopy(bukkitRecipe.craftItem(bukkitInventory));
	}
	
	@Override
	public boolean c() {
		return bukkitRecipe.isHidden();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof Bukkit2NMSRecipe)) return false;
		Bukkit2NMSRecipe that = (Bukkit2NMSRecipe) object;
		return this.bukkitRecipe.equals(that.bukkitRecipe);
	}
	
	@Override
	public int hashCode() {
		return bukkitRecipe.hashCode();
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public Recipe toBukkitRecipe() {
		//we try the best we can to do something meaningful.
		
		if (bukkitRecipe instanceof CRShapedRecipe) {
			return ((CRShapedRecipe) bukkitRecipe).getHandle().toBukkitRecipe();
			
		} else if (bukkitRecipe instanceof CRShapelessRecipe) {
			return ((CRShapelessRecipe) bukkitRecipe).getHandle().toBukkitRecipe();
			
			
		} else if (bukkitRecipe instanceof ShapedRecipe) {
			//try our very best to do something useful
			ShapedRecipe shapedRecipe = (ShapedRecipe) bukkitRecipe;
			
			NamespacedKey pluginKey = new NamespacedKey(CustomRecipesPlugin.getInstance(), CUSTOM_RECIPES_BUKKITRECIPE_KEY + keyCount.getAndIncrement());
			org.bukkit.inventory.ShapedRecipe bukkitShapedRecipe = new org.bukkit.inventory.ShapedRecipe(pluginKey, bukkitRecipe.getResult());
			
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
			
		} else if (bukkitRecipe instanceof ShapelessRecipe) {
			//try our very best to do something useful
			ShapelessRecipe shapelessRecipe = (ShapelessRecipe) bukkitRecipe;
			
			NamespacedKey pluginKey = new NamespacedKey(CustomRecipesPlugin.getInstance(), CUSTOM_RECIPES_BUKKITRECIPE_KEY + keyCount.getAndIncrement());
			org.bukkit.inventory.ShapelessRecipe bukkitShapelessRecipe = new org.bukkit.inventory.ShapelessRecipe(pluginKey, bukkitRecipe.getResult());
			
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
		
		//Recipe was neither a Shaped nor a Shapeless recipe, return a generic Recipe. 
		return bukkitRecipe::getResult;
	}	

}
