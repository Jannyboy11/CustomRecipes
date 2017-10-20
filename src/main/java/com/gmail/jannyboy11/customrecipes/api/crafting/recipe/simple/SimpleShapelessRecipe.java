package com.gmail.jannyboy11.customrecipes.api.crafting.recipe.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapelessRecipe;

/**
 * Represents a shapeless recipe that aims to mirror the vanilla implementation.
 *  
 * @author Jan
 */
public final class SimpleShapelessRecipe extends SimpleCraftingRecipe implements ShapelessRecipe {
	
	protected List<ChoiceIngredient> ingredients = new ArrayList<>();
	
	/**
	 * Construct a shapeless recipe with the given ItemStack as the result.
	 * 
	 * @param key the key of the recipe
	 * @param result the result of the recipe
	 */
	public SimpleShapelessRecipe(NamespacedKey key, ItemStack result) {
		super(key, result);
	}
	
	/**
	 * Construct a shapeless recipe with the given result and ingredients.
	 * 
	 * @param result the result of the recipe
	 * @param ingredients the ingredients list
	 */
	public SimpleShapelessRecipe(NamespacedKey key, ItemStack result, List<? extends ChoiceIngredient> ingredients) {
		this(key, result);
		setIngredients(ingredients);
	}
	
	/**
	 * Constructor for deserialization.
	 * 
	 * @param map the serialized fields
	 */
	@SuppressWarnings("unchecked")
    public SimpleShapelessRecipe(Map<String, Object> map) {
		super(map);
		this.ingredients = (List<ChoiceIngredient>) map.get("ingredients");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = super.serialize();
		map.put("ingredients", getIngredients());
		return map;
	}
	
	/**
	 * Set the ingredients list. None of the ingredients in the list can be null.
	 * 
	 * @param ingredients the ingredients list
	 */
	public void setIngredients(List<? extends ChoiceIngredient> ingredients) {
		Objects.requireNonNull(ingredients);
		if (ingredients.stream().anyMatch(Objects::isNull)) throw new IllegalArgumentException("ingredients cannot be null");
		
		this.ingredients = new ArrayList<>(ingredients);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends ChoiceIngredient> getIngredients() {
		return Collections.unmodifiableList(ingredients);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof ShapelessRecipe)) return false;
		ShapelessRecipe that = (ShapelessRecipe) o;
		
		return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getIngredients(), that.getIngredients()) &&
				Objects.equals(this.isHidden(), that.isHidden()) && Objects.equals(this.getGroup(), that.getGroup());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getResult(), getIngredients(), isHidden(), getGroup());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "{" + 
			"result=" + result +
			",ingredients=" + ingredients +
			",hidden=" + hidden +
			",group=" + group +				
			"}";
	}

}
