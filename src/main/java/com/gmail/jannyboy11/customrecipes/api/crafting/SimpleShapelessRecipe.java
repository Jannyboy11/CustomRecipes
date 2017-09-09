package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.vanilla.recipe.ShapelessRecipe;

/**
 * Represents a shapeless recipe that aims to mirror the vanilla implementation.
 *  
 * @author Jan
 */
public final class SimpleShapelessRecipe extends SimpleCraftingRecipe implements ShapelessRecipe {
	
	protected List<ChoiceIngredient> ingredients = new ArrayList<>();
	
	/**
	 * Construct a shapeless recipe with the given ItemStack as the result.
	 * @param result the result of the recipe
	 */
	public SimpleShapelessRecipe(ItemStack result) {
		super(result);
	}
	
	/**
	 * Construct a shapeless recipe with the given result and ingredients.
	 * 
	 * @param result the result of the recipe
	 * @param ingredients the ingredients list
	 */
	public SimpleShapelessRecipe(ItemStack result, List<? extends ChoiceIngredient> ingredients) {
		this(result);
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
	public boolean matches(CraftingInventory craftingInventory, World world) {
		final List<ChoiceIngredient> ingredients = new ArrayList<>(this.ingredients);
		final List<ItemStack> contents = Arrays.asList(craftingInventory.getMatrix())
				.stream().filter(i -> !InventoryUtils.isEmptyStack(i))
				.collect(Collectors.toList());
		
		for (ItemStack stack : contents) {
			boolean match = false;
			for (int ingredientIndex = 0; ingredientIndex < ingredients.size(); ingredientIndex++) {
				ChoiceIngredient ingredient = ingredients.get(ingredientIndex);
				if (ingredient.isIngredient(stack)) {
					ingredients.remove(ingredientIndex);
					match = true;
					break;
				}
			}
			
			//there was no matching ingredient for the current ItemStack
			if (!match) return false;
		}
		
		//return true if there are no unused ingredients left over
		return ingredients.isEmpty();
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
