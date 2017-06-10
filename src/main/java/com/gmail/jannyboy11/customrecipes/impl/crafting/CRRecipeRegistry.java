package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Iterators;

import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;

public class CRRecipeRegistry extends RegistryMaterials {

	private final RegistryMaterials<MinecraftKey, IRecipe> registry;

	private final Set<Predicate<? super IRecipe>> disablers = new HashSet<>();

	public CRRecipeRegistry(RegistryMaterials<MinecraftKey, IRecipe> registry) {
		this.registry = registry;
	}
	
	public boolean disableIf(Predicate<? super IRecipe> predicate) {
		return disablers.add(predicate);
	}
	

	public void a(int i, MinecraftKey key, IRecipe value) {
		registry.a(i, key, value);
	}

	public IRecipe get(MinecraftKey key) {
		return registry.get(key);
	}

	public MinecraftKey b(IRecipe recipe) {
		return registry.b(recipe);
	}

	public boolean d(MinecraftKey key) {
		return registry.d(key);
	}

	public int a(IRecipe recipe) {
		return registry.a(recipe);
	}

	public IRecipe getId(int id) {
		return registry.getId(id);
	}

	public Iterator<IRecipe> iterator() {
		return Iterators.filter(registry.iterator(), r -> disablers.stream().noneMatch(p -> p.test(r)));
	}

}
