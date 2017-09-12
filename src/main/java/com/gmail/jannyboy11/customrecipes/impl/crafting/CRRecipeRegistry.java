package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;

public class CRRecipeRegistry extends RegistryMaterials {

	private final BiMap<MinecraftKey, IRecipe> byKey = HashBiMap.create(256);
	private final BiMap<Integer, IRecipe> byId = HashBiMap.create(256);
	
	private final BiMap<IRecipe, MinecraftKey> toKey = byKey.inverse();
	private final BiMap<IRecipe, Integer> toId = byId.inverse();
	
	private IRecipe[] lazyRandomValuesProvider;
	
	
	public CRRecipeRegistry() {
		//default constructor, empty registry.
	}

	public CRRecipeRegistry(RegistryMaterials<MinecraftKey, IRecipe> registry) {
		//copy constructor, copy all values over
		registry.iterator().forEachRemaining(recipe -> {
			MinecraftKey key = registry.b(recipe);
			int id = registry.a(recipe);
			
			a(id, key, recipe);
		});
	}
	
	
	
	private void stateChanged() {
		this.lazyRandomValuesProvider = null;
	}
	
	
	
	
	// ----- methods from RegistrySimple -----
	
	/**
	 * Get the recipe by its key.
	 * 
	 * @param key the key
	 * @return the recipe, or null if no recipe was associated with that key
	 */
	public IRecipe get(MinecraftKey key) {
		return byKey.get(key);
	}
	@Override
	public IRecipe get(Object key) {
		return get((MinecraftKey) key);
	}
	
	/**
	 * Put a recipe to this registry.
	 * 
	 * @deprecated flawed implementation which doesn't register an ID.
	 * Use {@link CRRecipeRegistry#a(int, MinecraftKey, IRecipe)} instead.
	 * 
	 * @param key the key
	 * @param recipe the recipe
	 */
	@Deprecated
	public void a(MinecraftKey key, IRecipe recipe) {
		byKey.forcePut(key, recipe);
		stateChanged();
	}
	@Override
	public void a(Object key, Object value) {
		a((MinecraftKey) key, (IRecipe) value);
	}
	
	/**
	 * Get a keyset of all registered recipes.
	 */
	@Override
	public Set<MinecraftKey> keySet() {
		return Collections.unmodifiableSet(byKey.keySet());
	}
	
	/**
	 * Get a random value out of this registry.
	 */
	@Override
	public IRecipe a(Random random) {
		if (lazyRandomValuesProvider == null) {
			//lazy initialization
			int size = byKey.size();
			lazyRandomValuesProvider = new IRecipe[size];
			Iterator<IRecipe> iterator = iterator();
			for (int i = 0; i < size; i++) {
				lazyRandomValuesProvider[i] = iterator.next();
			}
		}
		
		return lazyRandomValuesProvider[random.nextInt(lazyRandomValuesProvider.length)];
	}
	
	/**
	 * Contains key
	 * 
	 * @param key the key of a potential recipe
	 * @return true if the key was registered for a recipe, otherwise false
	 */
	public boolean d(MinecraftKey key) {
		return byKey.containsKey(key);
	}
	@Override
	public boolean d(Object key) {
		return d((MinecraftKey) key);
	}

	/**
	 * An iterator which iterates over all registered recipes.
	 */
	@Override
	public Iterator<IRecipe> iterator() {
		return byKey.values().iterator();
	}
	
	// ----- methods from RegistryMaterials -----
	
	/**
	 * Register a new recipe
	 * 
	 * @param id the int unique identifier.
	 */
	public void a(int id, MinecraftKey key, IRecipe recipe) {
		byKey.forcePut(key, recipe);
		byId.forcePut(id, recipe);
		stateChanged();
	}
	@Override
	public void a(int id, Object key, Object value) {
		a(id, (MinecraftKey) key, (IRecipe) value);
	}

	/**
	 * Get the key from the reverse registry.
	 * 
	 * @param recipe the recipe
	 * @return a minecraft key or null if the recipe was not registered
	 */
	public MinecraftKey b(IRecipe recipe) {
		return toKey.get(recipe);
	}
	@Override
	public MinecraftKey b(Object recipe) {
		return b((IRecipe) recipe);
	}

	/**
	 * Get the int id from the reverse registry.
	 * 
	 * @param recipe the recipe
	 * @return the integer id, or -1 if no id was registered for the recipe
	 */
	public int a(IRecipe recipe) {
		return toId.getOrDefault(recipe, -1);
	}
	@Override
	public int a(Object recipe) {
		return a((IRecipe) recipe);
	}

	/**
	 * Get the object by its integer id.
	 * 
	 * @param id the integer id
	 * @return IRecipe the recipe that is associated with hat key, or null if no recipe was available for that key.
	 */
	@Override
	public IRecipe getId(int id) {
		return byId.get(id);
	}
	
	// ----- extra methods needed for CustomRecipes -----
	
	/**
	 * Removes a recipe from this registry.
	 * 
	 * @param recipe a recipe
	 * @return whether the recipe was removed
	 */
	public MinecraftKey removeRecipe(IRecipe recipe) {
		MinecraftKey removed = toKey.remove(recipe);
		toId.remove(recipe);
		if (removed != null) stateChanged();
		return removed;
	}
	
	/**
	 * Remove a recipe from this registry.
	 * 
	 * @param key the key of a recipe
	 * @return the recipe that was associated with the key, or null if there was no association
	 */
	public IRecipe removeRecipe(MinecraftKey key) {
		IRecipe recipe = byKey.remove(key);
		if (recipe != null) {
			toId.remove(recipe);
			stateChanged();
			return recipe;
		}
		
		return null;
	}
	
	/**
	 * Remove a recipe from this registry.
	 * 
	 * @param id the integer id
	 * @return the recipe that was associated with the id, or null if there was no association
	 */
	public IRecipe removeRecipe(int id) {
		IRecipe recipe = byId.remove(id);
		if (recipe != null) {
			toKey.remove(recipe);
			stateChanged();
			return recipe;
		}
		
		return null;
	}

	/**
	 * Check whether a recipe is registered.
	 * 
	 * @param recipe the recipe
	 * @return true if the recipe is registered, otherwise false
	 */
	public boolean isRegistered(IRecipe recipe) {
		return toKey.containsKey(recipe);
	}
	
}
