package com.gmail.jannyboy11.customrecipes.api.crafting;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Base class for crafting recipes.
 * 
 * @author Jan
 */
public abstract class SimpleCraftingRecipe implements CraftingRecipe, ConfigurationSerializable {
	
	protected ItemStack result;
	protected boolean hidden = false;
	protected String group = "";
	
	/**
	 * Construct a crafting recipe with the given ItemStack as the result
	 * @param result the result of the recipe
	 */
	protected SimpleCraftingRecipe(ItemStack result) {
		this.result = result;
	}
	
	/**
	 * Constructor for deserialization
	 * @param map the serialized fields
	 */
	protected SimpleCraftingRecipe(Map<String, Object> map) {
		this.result = (ItemStack) map.get("result");
		Object hidden = map.get("hidden");
		if (hidden != null) this.hidden = Boolean.valueOf(hidden.toString());
		Object group = map.get("group");
		if (group != null) this.group = group.toString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>();
		map.put("result", getResult());
		if (isHidden()) map.put("hidden", isHidden());
		if (hasGroup()) map.put("group", getGroup());
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack getResult() {
		return result;
	}
	
	/**
	 * Set the result of this recipe
	 * @param result the result
	 */
	public void setResult(ItemStack result) {
		this.result = result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemStack craftItem(CraftingInventory craftingInventory) {
		return result == null ? null : result.clone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isHidden() {
		return hidden;
	}
	
	/**
	 * Set the hidden field of this recipe
	 * @param hidden whether this recipe is hidden
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGroup() {
		return group;
	}
	
	/**
	 * Set the group this recipe belongs to
	 * @param group the group
	 */
	public void setGroup(String group) {
		this.group = group == null ? "" : group;
	}
	
}
