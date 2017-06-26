package com.gmail.jannyboy11.customrecipes.impl.furnace;

import java.util.Map;
import java.util.Objects;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.RecipesFurnace;

public class CRFurnaceRecipe implements com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe {

	private final FurnaceRecipe nmsRecipe;
	
	public CRFurnaceRecipe(FurnaceRecipe nmsRecipe) {
		this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
	}

	@Override
	public CraftItemStack getIngredient() {
		return CraftItemStack.asCraftMirror(nmsRecipe.getIngredient());
	}

	@Override
	public CraftItemStack getResult() {
		return CraftItemStack.asCraftMirror(nmsRecipe.getResult());
	}

	@Override
	public float getXp() {
		return nmsRecipe.getXp();
	}

	@Override
	public void setIngredient(org.bukkit.inventory.ItemStack ingredient) {
		if (ingredient instanceof CraftItemStack) {
			setIngredient((CraftItemStack) ingredient);
		} else {
			setIngredient(CraftItemStack.asCraftCopy(ingredient));
		}
	}

	@Override
	public void setResult(org.bukkit.inventory.ItemStack result) {
		if (result instanceof CraftItemStack) {
			setResult((CraftItemStack) result);
		} else {
			setResult(CraftItemStack.asCraftCopy(result));
		}
	}

	@Override
	public void setXp(float xp) {
		nmsRecipe.setXp(xp);
	}
	
	public void setIngredient(CraftItemStack ingredient) {
		nmsRecipe.setIngredient(CraftItemStack.asNMSCopy(ingredient));
	}

	public void setResult(CraftItemStack result) {
		nmsRecipe.setResult(CraftItemStack.asNMSCopy(result));		
	}
	
	@Override
	public boolean hasXp() {
		return nmsRecipe.hasXp();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FurnaceRecipe)) return false;
		
		FurnaceRecipe that = (FurnaceRecipe) o;
		return Objects.equals(getIngredient(), that.getIngredient()) &&
				Objects.equals(getResult(), that.getResult()) &&
				Objects.equals(Float.floatToIntBits(getXp()), Float.floatToIntBits(that.getXp()));
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getIngredient(), getResult(), getXp());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{nmsRecipe=" + nmsRecipe + "}";
	}
	
	public static CRFurnaceRecipe fromSimple(com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe simple, RecipesFurnace recipesFurnace, Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps) {
		if (simple instanceof CRFurnaceRecipe) {
			return (CRFurnaceRecipe) simple;
		}
		
		ItemStack nmsIngredient = CraftItemStack.asNMSCopy(simple.getIngredient());
		ItemStack nmsResult = CraftItemStack.asNMSCopy(simple.getResult());
		float xp = simple.getXp();
		
		results.put(nmsIngredient, nmsResult);
		if (xp > 0F) xps.put(nmsIngredient, xp);
		
		return new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace, results, xps, nmsIngredient));
	}
	
	
	public static class FurnaceRecipe {
		
		private final RecipesFurnace recipesFurnace;
		private final Map<ItemStack, ItemStack> results;
		private final Map<ItemStack, Float> xps;
		private ItemStack source;
		
		public FurnaceRecipe(RecipesFurnace recipesFurnace, Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps) {
			this.recipesFurnace = recipesFurnace;
			this.results = results;
			this.xps = xps;
		}

		public FurnaceRecipe(RecipesFurnace recipesFurnace, Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps, ItemStack ingredient) {
			this(recipesFurnace, results, xps);
			this.source = ingredient;
		}

		public void setXp(float xp) {
			if (xp > 0F) {
				xps.put(source, xp);
			} else {
				xps.remove(source);
			}
		}

		public void setResult(ItemStack result) {
			results.put(source, result);
		}

		public void setIngredient(ItemStack ingredient) {
			ItemStack result = results.remove(source);
			if (result != null) {
				results.put(ingredient, result);
				Float xp = xps.remove(source);
				if (xp != null) xps.put(ingredient, xp);
			}
			
			this.source = ingredient;
		}

		public float getXp() {
			return recipesFurnace.b(source);
		}

		public ItemStack getResult() {
			return recipesFurnace.getResult(source);
		}

		public ItemStack getIngredient() {
			return source;
		}
		
		public boolean hasXp() {
			return xps.containsKey(source);
		}
		
		//apparently net.minecraft.server.v1_XX_RX.ItemStack doesn't override equals() and hashCode()!
		
		@Override
		public String toString() {
			return getClass().getName() + "{" +
					"source= " + source +
					",result()=" + getResult() +
					",xp()=" + xps.get(source) +
					"}";
		}
		
	}

}
