package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapedRecipe;

public final class SimpleShapedRecipe implements ShapedRecipe {
	
	private final NamespacedKey key;
	private ItemStack result;
	private int width = 3;
	private int heigth = 3;
	@SuppressWarnings("serial")
	private List<ChoiceIngredient> ingredients = new ArrayList<ChoiceIngredient>(width * heigth) {
		{
			for (int i = 0; i < width * heigth; i++) {
				add(SimpleChoiceIngredient.ACCEPTING_EMPTY);
			}
		}
	};
	private boolean hidden;
	private String group = "";
	
	public SimpleShapedRecipe(NamespacedKey key) {
		this.key = Objects.requireNonNull(key);
	}
	
	public SimpleShapedRecipe(NamespacedKey key, ItemStack result) {
		this(key);
		setResult(result);
	}
	
	public SimpleShapedRecipe(NamespacedKey key, ItemStack result, int width, int heigth, List<? extends ChoiceIngredient> ingredients) {
		this(key, result);
		setIngredients(width, heigth, ingredients);
	}
	
	public void setGroup(String group) {
		this.group = group == null ? "" : group;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
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
				if (matrixMatch(craftingInventory, world, w, h, true)) {
					return true;
				}
				if (matrixMatch(craftingInventory, world, w, h, false)) {
					return true;
				}
			}
		}
				
        return false;
	}
	
	private boolean matrixMatch(CraftingInventory craftingInventory, World world, int maxColumns, int maxRows, boolean mirrored) {
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
	public ItemStack craftItem(CraftingInventory craftingInventory) {
		return result == null ? null : result.clone();
	}

	@Override
	public ItemStack getResult() {
		return result;
	}

	@Override
	public List<? extends ItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
		return Arrays.stream(craftingInventory.getMatrix())
				.map(itemStack -> {
					if (itemStack == null || itemStack.getAmount() <= 1) return null;
					ItemStack clone = itemStack.clone();
					clone.setAmount(itemStack.getAmount() - 1);
					return clone;
				}).collect(Collectors.toList());
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public NamespacedKey getKey() {
		return key;
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
	public String getGroup() {
		return group;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapedRecipe)) return false;
		ShapedRecipe that = (ShapedRecipe) o;
		
		return Objects.equals(this.key, that.getKey()) && Objects.equals(this.result, that.getResult()) &&
				Objects.equals(this.width, that.getWidth()) && Objects.equals(this.heigth, that.getHeight()) &&
				Objects.equals(this.ingredients, that.getIngredients()) && Objects.equals(this.hidden, that.isHidden()) &&
				Objects.equals(this.group, that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(key, result, width, heigth, ingredients, hidden, group);
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"key=" + key +
			",result=" + result +
			",width=" + width +
			",heigth=" + heigth +
			",ingredients=" + ingredients +
			",hidden=" + hidden +
			",group=" + group +				
			"}";
	}

}
