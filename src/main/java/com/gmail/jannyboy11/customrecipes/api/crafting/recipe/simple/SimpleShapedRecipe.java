package com.gmail.jannyboy11.customrecipes.api.crafting.recipe.simple;

import java.util.Map;
import java.util.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.Shape;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;

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
