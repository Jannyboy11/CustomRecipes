package com.gmail.jannyboy11.customrecipes.impl.furnace;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.RecipesFurnace;

public class CRFurnaceRecipe implements com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe, NBTSerializable {

	private final FurnaceRecipe nmsRecipe;
	private final boolean vanilla;
	
	public CRFurnaceRecipe(FurnaceRecipe nmsRecipe) {
		this(nmsRecipe, false);
	}
	
	public CRFurnaceRecipe(FurnaceRecipe nmsRecipe, boolean vanilla) {
		this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
		this.vanilla = vanilla;
	}
	
	public CRFurnaceRecipe(NBTTagCompound compound) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		nmsRecipe = new FurnaceRecipe(recipesFurnace, new HashMap<>(), new HashMap<>());
		
		nmsRecipe.setIngredient(NBTUtil.deserializeItemStack(compound.getCompound("ingredient")));
		nmsRecipe.setResult(NBTUtil.deserializeItemStack(compound.getCompound("result")));
		if (compound.hasKeyOfType("xp", 5 /*float*/)) nmsRecipe.setXp(compound.getFloat("xp"));
		
		vanilla = "vanilla".equals(compound.getString("type"));
	}

	public NBTTagCompound serializeToNbt() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.set("ingredient", NBTUtil.serializeItemStack(nmsRecipe.getIngredient()));
		tag.set("result", NBTUtil.serializeItemStack(nmsRecipe.getResult()));
		if (hasXp()) tag.setFloat("xp", nmsRecipe.getXp());
		if (vanilla) tag.setString("type", "vanilla");
		return tag;
	}
	
	public static CRFurnaceRecipe deserialzeFromNbt(NBTTagCompound compound, boolean vanilla) {
		RecipesFurnace recipesFurnace = RecipesFurnace.getInstance();
		FurnaceRecipe nmsRecipe = vanilla ?
				new FurnaceRecipe(recipesFurnace, recipesFurnace.recipes, CRFurnaceManager.vanillaXp(recipesFurnace)) :
				new FurnaceRecipe(recipesFurnace, recipesFurnace.customRecipes, recipesFurnace.customExperience);
		
		nmsRecipe.setIngredient(NBTUtil.deserializeItemStack(compound.getCompound("ingredient")));
		nmsRecipe.setResult(NBTUtil.deserializeItemStack(compound.getCompound("result")));
		if (compound.hasKeyOfType("xp", 5 /*float*/)) nmsRecipe.setXp(compound.getFloat("xp"));
		
		return new CRFurnaceRecipe(nmsRecipe, vanilla);
	}
	
	public FurnaceRecipe getHandle() {
		return nmsRecipe;
	}
	
	@Override
	public Map<String, Object> serialize() {
		return NBTSerializable.super.serialize();
	}
	
	public static CRFurnaceRecipe deserialize(Map<String, Object> map) {
		return deserialzeFromNbt(NBTUtil.fromMap(map), false);
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
	
	
	public boolean isVanilla() {
		return vanilla;
	}
	
	
	
	@SuppressWarnings("unlikely-arg-type")
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

	public static CRFurnaceRecipe registerFromSimple(boolean vanilla, com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe simple, RecipesFurnace recipesFurnace, Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps) {
		if (simple instanceof CRFurnaceRecipe) {
			CRFurnaceRecipe crFurnaceRecipe = (CRFurnaceRecipe) simple;
			FurnaceRecipe nmsRecipe = crFurnaceRecipe.nmsRecipe;
			ItemStack ingredient = nmsRecipe.getIngredient();
			ItemStack result = nmsRecipe.getResult();
			float xp = nmsRecipe.getXp();
			
			nmsRecipe.recipesFurnace = recipesFurnace;
			nmsRecipe.results = results;
			nmsRecipe.xps = xps;
			nmsRecipe.setIngredient(ingredient);
			nmsRecipe.setResult(result);
			nmsRecipe.setXp(xp);
			
			return crFurnaceRecipe;
		}
		
		ItemStack nmsIngredient = CraftItemStack.asNMSCopy(simple.getIngredient());
		ItemStack nmsResult = CraftItemStack.asNMSCopy(simple.getResult());
		float xp = simple.getXp();
		
		results.put(nmsIngredient, nmsResult);
		if (xp > 0F) xps.put(nmsIngredient, xp);
		
		return new CRFurnaceRecipe(new FurnaceRecipe(recipesFurnace, results, xps, nmsIngredient), vanilla);
	}
	
	
	public static class FurnaceRecipe {
		
		private RecipesFurnace recipesFurnace;
		private Map<ItemStack, ItemStack> results;
		private Map<ItemStack, Float> xps;
		private ItemStack source;
		
		public FurnaceRecipe(RecipesFurnace recipesFurnace, Map<ItemStack, ItemStack> results, Map<ItemStack, Float> xps) {
			this.recipesFurnace = Objects.requireNonNull(recipesFurnace);
			this.results = Objects.requireNonNull(results);
			this.xps = Objects.requireNonNull(xps);
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
			return xps.entrySet().stream().filter(e -> CRFurnaceManager.furnaceEquals(recipesFurnace, e.getKey(), source))
					.findAny().map(Entry::getValue)
					.orElse(0F);
		}

		public ItemStack getResult() {
			return results.entrySet().stream().filter(e -> CRFurnaceManager.furnaceEquals(recipesFurnace, e.getKey(), source))
					.findAny().map(Entry::getValue)
					.orElse(ItemStack.a);
		}

		public ItemStack getIngredient() {
			return source;
		}
		
		public boolean hasXp() {
			return xps.keySet().stream().anyMatch(ingrStack -> CRFurnaceManager.furnaceEquals(recipesFurnace, ingrStack, source));
		}
		
		//net.minecraft.server.v1_XX_RX.ItemStack doesn't override equals() and hashCode()!
		public boolean equals(Object o) {
			if (o == this) return true;
			if (!(o instanceof FurnaceRecipe)) return false;
			FurnaceRecipe that = (FurnaceRecipe) o;

			return CRFurnaceManager.furnaceEquals(recipesFurnace, this.source, that.source) && this.results == that.results && this.xps == that.xps;	
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(source, getResult(), getXp());
		}
		
		@Override
		public String toString() {
			return getClass().getName() + "{" +
					"source= " + source +
					",result()=" + getResult() +
					",xp()=" + xps.get(source) +
					"}";
		}

		public void setIngredientMap(Map<ItemStack, ItemStack> ingredientMap) {
			this.results = Objects.requireNonNull(ingredientMap);
		}

		public void setXpMap(Map<ItemStack, Float> xpMap) {
			this.xps = Objects.requireNonNull(xpMap);
		}
		
	}

}
