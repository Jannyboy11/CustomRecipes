package com.gmail.jannyboy11.customrecipes.api.crafting.recipe.simple;

import java.util.Map;
import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.Shape;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.util.GridView;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;

/**
 * Represents a shaped recipe that aims to mirror the vanilla shaped recipe implementation.
 * 
 * @author Jan
 */
public final class SimpleShapedRecipe extends SimpleCraftingRecipe implements ShapedRecipe {
	
    protected final Shape shape;
	
	/**
	 * Construct a shaped recipe with the given ItemStack as the result
	 * 
	 * @param key the key of the recipe
	 * @param result the result of the recipe
	 */
	public SimpleShapedRecipe(NamespacedKey key, ItemStack result, Shape shape) {
		super(key, result);
		this.shape = shape;
	}
	
	/**
	 * Constructor for deserialization.
	 * 
	 * @param map the serialized fields
	 */
	@SuppressWarnings("unchecked")
    public SimpleShapedRecipe(Map<String, Object> map) {
		super(map);
		this.shape = (Shape) map.get("shape");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put("shape", getShape());
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public Shape getShape() {
        return shape;
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		int width, height;
		
		//check boundaries for the crafting inventory
		InventoryType type = craftingInventory.getType();
		switch(type) {
			case CRAFTING:
				width = height = 2;
				break;
			case WORKBENCH:
				width = height = 3;
				break;
			default: return false; //unknown crafting inventory type.
		}
		
		GridView gridInventory = new GridView(craftingInventory, width, height);
		Shape shape = getShape();
		
		final int maxAddX = width - shape.getWidth();
		final int maxAddY = height - shape.getHeight();
		
		for (int addX = 0; addX <= maxAddX; addX++) {
			for (int addY = 0; addY <= maxAddY; addY++) {
				if (matrixMatch(gridInventory, addX, addY, true)) {
					return true;
				}
				if (matrixMatch(gridInventory, addX, addY, false)) {
					return true;
				}
			}
		}
				
        return false;
	}
	
	
	private boolean matrixMatch(GridView craftingInventory, int addX, int addY, boolean mirrored) {
	    Shape shape = getShape();
	    
	    Map<Character, ? extends CraftingIngredient> ingredients = shape.getIngredientMap();
	    String[] pattern = shape.getPattern();
	    
	    for (int shapeY = 0; shapeY < shape.getHeight(); shapeY++) {
	        for (int shapeX = 0; shapeX < shape.getWidth(); shapeX++) {
	            
	            final int gridX = addX + (mirrored ? shape.getWidth() - 1 - shapeX : shapeX);
                final int gridY = addY + shapeY;
                                    
                char key = pattern[shapeY].charAt(shapeX);
                CraftingIngredient ingredient = ingredients.get(key);
                if (ingredient == null) ingredient = InventoryUtils::isEmptyStack;
                
                ItemStack input = craftingInventory.getItem(gridX, gridY);
                if (!ingredient.isIngredient(input)) return false;
	        }
	    }
	  
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapedRecipe)) return false;
		ShapedRecipe that = (ShapedRecipe) o;
		
		return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getShape(), that.getShape()) &&
				Objects.equals(this.isHidden(), that.isHidden()) &&	Objects.equals(this.getGroup(), that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getResult(), getShape(), isHidden(), getGroup());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"result()=" + getResult() +
			",shape()=" + getShape() +
			",hidden()=" + isHidden() +
			",group()=" + getGroup() +				
			"}";
	}


}
