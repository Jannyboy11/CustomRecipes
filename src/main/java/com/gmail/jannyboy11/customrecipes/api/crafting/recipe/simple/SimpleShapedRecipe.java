package com.gmail.jannyboy11.customrecipes.api.crafting.recipe.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.SimpleChoiceIngredient;

/**
 * Represents a shaped recipe that aims to mirror the vanilla shaped recipe implementation.
 * 
 * @author Jan
 */
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
	
	/**
	 * Construct a shaped recipe with the given ItemStack as the result
	 * 
	 * @param key the key of the recipe
	 * @param result the result of the recipe
	 */
	public SimpleShapedRecipe(NamespacedKey key, ItemStack result) {
		super(key, result);
	}
	
	/**
	 * Construct a shaped recipe with given result, with, height and ingredients.
	 * 
	 * @param key the key of the recipe
	 * @param result the result of the recipe
	 * @param width the width (1-3)
	 * @param heigth the height (1-3)
	 * @param ingredients the ingredients
	 */
	public SimpleShapedRecipe(NamespacedKey key, ItemStack result, int width, int heigth, List<? extends ChoiceIngredient> ingredients) {
		this(key, result);
		setIngredients(width, heigth, ingredients);
	}
	
	/**
	 * Constructor for deserialization.
	 * 
	 * @param map the serialized fields
	 */
	@SuppressWarnings("unchecked")
    public SimpleShapedRecipe(Map<String, Object> map) {
		super(map);
		this.width = Integer.valueOf(map.get("width").toString());
		this.heigth = Integer.valueOf(map.get("heigth").toString());
		this.ingredients = (List<ChoiceIngredient>) map.get("ingredients");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put("width", getWidth());
		map.put("height", getHeight());
		map.put("ingredients", getIngredients());
		return map;
	}
	
	/**
	 * Set the ingredients.
	 * 
	 * @param width the width of the shape
	 * @param heigth the heigth of the shape
	 * @param ingredients the ingredients. The list size should be equal to width * heigth
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		int width, heigth;
		
		//check boundaries for the crafting inventory
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
				Ingredient choiceIngredient = InventoryUtils::isEmptyStack;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getHeight() {
		return heigth;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ChoiceIngredient> getIngredients() {
		return Collections.unmodifiableList(ingredients);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapedRecipe)) return false;
		ShapedRecipe that = (ShapedRecipe) o;
		
		return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getIngredients(), that.getIngredients()) &&
				Objects.equals(this.getWidth(), that.getWidth()) && Objects.equals(this.getHeight(), that.getHeight()) &&
				Objects.equals(this.isHidden(), that.isHidden()) &&	Objects.equals(this.getGroup(), that.getGroup());
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
