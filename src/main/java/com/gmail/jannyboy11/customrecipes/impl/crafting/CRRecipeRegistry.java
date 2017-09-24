package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;

public class CRRecipeRegistry extends RegistryMaterials {

	private final BiMap<MinecraftKey, NMSCraftingRecipe<?>> keyToRecipe = HashBiMap.create(256);
	private final BiMap<NMSCraftingRecipe<?>, MinecraftKey> recipeToKey = keyToRecipe.inverse();
	
	private final BiMap<MinecraftKey, Integer> keyToId = HashBiMap.create(256);
	private final BiMap<Integer, MinecraftKey> idToKey = keyToId.inverse();
	
	//TODO use this to generate IDs when needed, also update it when new IDs are given.
	private int nextId = 0;
	
	private NMSCraftingRecipe<?>[] lazyRandomValuesProvider;
	
	
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
	public NMSCraftingRecipe<?> get(MinecraftKey key) {
		return keyToRecipe.get(key);
	}
	@Override
	public NMSCraftingRecipe<?> get(Object key) {
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
		a(nextId++, key, getNMSCraftingRecipe(recipe));
		stateChanged();
	}
	@Override
	public void a(Object key, Object value) {
		a((MinecraftKey) key, (IRecipe) value);
	}
	
	/**
	 * Get a key set of all registered recipes.
	 */
	@Override
	public Set<MinecraftKey> keySet() {
		return keyToRecipe.keySet();
	}
	
	/**
	 * Get a random value out of this registry.
	 */
	@Override
	public NMSCraftingRecipe<?> a(Random random) {
		if (lazyRandomValuesProvider == null) {
			//lazy initialization
			int size = keyToRecipe.size();
			lazyRandomValuesProvider = new NMSCraftingRecipe[size];
			Iterator<NMSCraftingRecipe<?>> iterator = iterator();
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
		return keyToRecipe.containsKey(key);
	}
	@Override
	public boolean d(Object key) {
		return d((MinecraftKey) key);
	}

	/**
	 * An iterator which iterates over all registered recipes.
	 */
	@Override
	public Iterator<NMSCraftingRecipe<?>> iterator() {
		return keyToRecipe.values().iterator();
	}
	
	// ----- methods from RegistryMaterials -----
	
	/**
	 * Register a new recipe
	 * 
	 * @param id the int unique identifier.
	 */
	public void a(int id, MinecraftKey key, IRecipe recipe) {
		keyToRecipe.forcePut(key, getNMSCraftingRecipe(recipe));
		idToKey.forcePut(getNewSafeId(id), key);
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
		return recipeToKey.get(recipe);
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
	    MinecraftKey key = recipeToKey.get(recipe);
	    if (key == null) return -1;
	    
		return keyToId.getOrDefault(key, -1);
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
	public NMSCraftingRecipe<?> getId(int id) {
		MinecraftKey key = idToKey.get(id);
		if (key == null) return null;
		return keyToRecipe.get(key);
	}
	
	// ----- extra methods needed for CustomRecipes -----
	
	private int getNewSafeId(final int id) {
        nextId = id >= nextId ? Math.max(nextId, id) + 1 : nextId;
	    
	    MinecraftKey key = idToKey.remove(id);
	    if (key != null) {
	        //id was already mapped to a recipe, re-map the old mapping!
	        
	        idToKey.put(nextId, key);
	        nextId++;
	        return id;
	    } else {
	        //id was not mapped

	        return id;
	    }
	}
	
	private NMSCraftingRecipe<?> getNMSCraftingRecipe(IRecipe vanilla) {
	    MinecraftKey key = b(vanilla);
	    if (key != null) {
	        return keyToRecipe.get(key);
	    } else if (vanilla instanceof NMSCraftingRecipe) {
	        return (NMSCraftingRecipe) vanilla;
	    } else {	        
	        return RecipeUtils.getNMSRecipe(vanilla);
	    }
	}
	
	/**
	 * Removes a recipe from this registry.
	 * 
	 * @param recipe a recipe
	 * @return the key that was removed, or null if no key was registered for the recipe
	 */
	public MinecraftKey removeRecipe(IRecipe recipe) {
		MinecraftKey removedKey = recipeToKey.remove(recipe);
		if (removedKey != null) {
		    keyToId.remove(removedKey);
		    stateChanged();
		}
		return removedKey;
	}
	
	/**
	 * Remove a recipe from this registry.
	 * 
	 * @param key the key of a recipe
	 * @return the recipe that was associated with the key, or null if there was no association
	 */
	public NMSCraftingRecipe<?> removeRecipe(MinecraftKey key) {
		NMSCraftingRecipe<?> recipe = keyToRecipe.remove(key);
		if (recipe != null) {
			keyToId.remove(key);
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
	public NMSCraftingRecipe<?> removeRecipe(int id) {
	    MinecraftKey key = idToKey.remove(id);
	    if (key == null) return null;
	    
		NMSCraftingRecipe<?> recipe = keyToRecipe.remove(key);
		if (recipe != null) {
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
	public boolean isRegistered(NMSCraftingRecipe<?> recipe) {
		return recipeToKey.containsKey(recipe);
	}
	
}
