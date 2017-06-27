package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;

public final class SimpleShapedRecipe extends SimpleCraftingRecipe implements ShapedRecipe {
	
	protected int width = 3;
	protected int heigth = 3;
	@SuppressWarnings("serial")
	protected List<ChoiceIngredient> ingredients = new ArrayList<ChoiceIngredient>(width * heigth) {
		{
			for (int i = 0; i < width * heigth; i++) {
				add(SimpleChoiceIngredient.ACCEPTING_EMPTY);
			}
		}
	};
	
	public SimpleShapedRecipe(ItemStack result) {
		super(result);
	}
	
	public SimpleShapedRecipe(ItemStack result, int width, int heigth, List<? extends ChoiceIngredient> ingredients) {
		this(result);
		setIngredients(width, heigth, ingredients);
	}
	
	public SimpleShapedRecipe(Map<String, Object> map) {
		super(map);
		this.width = Integer.valueOf(map.get("width").toString());
		this.heigth = Integer.valueOf(map.get("heigth").toString());
		this.ingredients = (List<ChoiceIngredient>) map.get("ingredients");
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put("width", getWidth());
		map.put("height", getHeight());
		map.put("ingredients", getIngredients());
		return map;
	}
	
	
	public void setIngredients(int width, int heigth, List<? extends ChoiceIngredient> ingredients) {
		if (width <= 0) throw new IllegalArgumentException("width cannot be smaller than zero!");
		if (heigth <= 0) throw new IllegalArgumentException("heigth cannot be smaller than zero!");
		
		this.width = width;
		this.heigth = heigth;
		
		Objects.requireNonNull(ingredients);
		if (ingredients.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("ingredients cannot be null");
		this.ingredients = new ArrayList<>(ingredients);
		for (int i = ingredients.size(); i < width * heigth; i++) {
			this.ingredients.add(SimpleChoiceIngredient.ACCEPTING_EMPTY);
		}
	}
	
	public void setResult(ItemStack result) {
		this.result = result;
	}
	

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		int width, heigth;
		
		//check bounaries for the craftinginventory;
		InventoryType type = craftingInventory.getType();
		switch(type) {
			case CRAFTING:
				width = heigth = 2;
				break;
			case WORKBENCH:
				width = heigth = 3;
				break;
			default: return false; //unknown crafting inventory type.
		}
		
		for (int w = 0; w <= width - this.width; w++) {
			for (int h = 0; h <= heigth - this.heigth; h++) {
				if (matrixMatch(craftingInventory, w, h, true)) {
					return true;
				}
				if (matrixMatch(craftingInventory, w, h, false)) {
					return true;
				}
			}
		}
				
        return false;
	}
	
	
	private boolean matrixMatch(CraftingInventory craftingInventory, int maxColumns, int maxRows, boolean mirrored) {
		for (int c = 0; c < 3; c++) {
			for (int r = 0; r < 3; r++) {
				int colNum = c - maxColumns;
				int rowNum = r - maxRows;
				CraftingIngredient choiceIngredient = InventoryUtils::isEmptyStack;
				if (colNum >= 0 && rowNum >= 0 && colNum < width && rowNum < heigth) {
					if (mirrored) {
						choiceIngredient = ingredients.get(width - 1 - colNum + rowNum * width);
					} else {
						choiceIngredient = ingredients.get(colNum + rowNum * width);
					}
				}
				
				if (!choiceIngredient.isIngredient(craftingInventory.getItem(colNum + rowNum * width))) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
		return Arrays.stream(craftingInventory.getMatrix())
				.map(itemStack -> {
					if (itemStack == null) return null;					
					ItemStack clone = itemStack.clone();
					MaterialData craftingResult = InventoryUtils.getSingleIngredientResult(itemStack.getData());
					if (craftingResult.getItemType() != Material.AIR) {
						clone.setData(craftingResult);
						return clone;
					}
					if (itemStack.getAmount() <= 1) return null;
					clone.setAmount(itemStack.getAmount() - 1);
					return clone;
				}).collect(Collectors.toList());
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return heigth;
	}

	@Override
	public List<ChoiceIngredient> getIngredients() {
		return Collections.unmodifiableList(ingredients);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapedRecipe)) return false;
		ShapedRecipe that = (ShapedRecipe) o;
		
		return Objects.equals(this.result, that.getResult()) && Objects.equals(this.ingredients, that.getIngredients()) &&
				Objects.equals(this.width, that.getWidth()) && Objects.equals(this.heigth, that.getHeight()) &&
				Objects.equals(this.hidden, that.isHidden()) &&	Objects.equals(this.group, that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(result, width, heigth, ingredients, hidden, group);
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"result=" + result +
			",width=" + width +
			",heigth=" + heigth +
			",ingredients=" + ingredients +
			",hidden=" + hidden +
			",group=" + group +				
			"}";
	}

}
