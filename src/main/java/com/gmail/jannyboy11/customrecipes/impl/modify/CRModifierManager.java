package com.gmail.jannyboy11.customrecipes.impl.modify;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.Inventory;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.modify.CraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;
import com.gmail.jannyboy11.customrecipes.api.modify.ModifierManager;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.modify.nms.ExtendedCraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.impl.modify.nms.NMSCountCraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.impl.modify.nms.NMSDamageCraftingIngredientModifier;
import com.gmail.jannyboy11.customrecipes.impl.modify.nms.NMSNBTCraftingIngredientModifier;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;

public class CRModifierManager implements ModifierManager {
    
    private final Map<MinecraftKey, Function<? super ItemStack, ? extends ExtendedCraftingIngredientModifier>> nmsCraftingIngredientModfiers = new LinkedHashMap<>();
    //TODO bukkit mappings?
    
    
    
    public CRModifierManager() {
        registerDefaults();
    }
    
    public void registerDefaults() {
        //crafting ingredient modifiers
        registerCraftingIngredientModifier(NMSNBTCraftingIngredientModifier.KEY, itemStack -> new NMSNBTCraftingIngredientModifier(itemStack.getTag()));
        registerCraftingIngredientModifier(NMSCountCraftingIngredientModifier.KEY, itemStack -> new NMSCountCraftingIngredientModifier(itemStack.getCount()));
        registerCraftingIngredientModifier(NMSDamageCraftingIngredientModifier.KEY, itemStack -> new NMSDamageCraftingIngredientModifier(itemStack.getData()));
        
        //crafting recipe modifiers
        //TODO use them as a form Function<? extends NMSCraftingModifier>?
        
        
        //TODO furnace ingredient modifiers
        //TODO furnace recipe modifiers
        
        
        //TODO crafting result?
    }
    
    
    protected void registerCraftingIngredientModifier(MinecraftKey modifierKey,
            Function<? super ItemStack, ? extends ExtendedCraftingIngredientModifier> nmsModifier) {
        nmsCraftingIngredientModfiers.put(modifierKey, nmsModifier);
    }
    
    protected void registerCraftingIngredientModifier(NamespacedKey modifierKey,
            Function<? super org.bukkit.inventory.ItemStack, ? extends CraftingIngredientModifier> bukkitModifierFunction) {
        MinecraftKey key = CraftNamespacedKey.toMinecraft(modifierKey);
        
        //TODO use a separate implementation class for this which can store the bukkit modifier function as a field?
        //TODO move this out to RecipeUtils?
        Function<? super ItemStack, ExtendedCraftingIngredientModifier> nmsModifierFunction =  itemStack -> {
            CraftItemStack bukkitStack = CraftItemStack.asCraftMirror(itemStack);
            CraftingIngredientModifier bukkitModifier = bukkitModifierFunction.apply(bukkitStack);
            ExtendedCraftingIngredientModifier nmsModifier = RecipeUtils.getNMSCraftingIngredientModifier(bukkitModifier);
            return nmsModifier;
        };
        registerCraftingIngredientModifier(key, nmsModifierFunction);
    }
    
    public Map<MinecraftKey, Function<? super ItemStack, ? extends ExtendedCraftingIngredientModifier>> getNMSCraftingIngredientModifiers() {
        return new LinkedHashMap<>(nmsCraftingIngredientModfiers);
    }
    
    public Map<NamespacedKey, Function<? super org.bukkit.inventory.ItemStack, ? extends CraftingIngredientModifier>> getCraftingIngredientModifiers() {
        return nmsCraftingIngredientModfiers.entrySet().stream()
                .collect(Collectors.toMap(e -> CraftNamespacedKey.fromMinecraft(e.getKey()), e -> {
                    Function<? super ItemStack, ? extends ExtendedCraftingIngredientModifier> nmsModifierFunction = e.getValue();
                    
                    //TODO use a separate implementation class for this which can store the nms modifier function as a field?
                    //TODO move this out to RecipeUtils?
                    Function<? super org.bukkit.inventory.ItemStack, ? extends CraftingIngredientModifier> bukkitModifierFunction = itemStack -> {
                        ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
                        ExtendedCraftingIngredientModifier nmsModifier = nmsModifierFunction.apply(nmsStack);
                        CraftingIngredientModifier bukkitModifier = RecipeUtils.getBukkitCraftingIngredientModifier(nmsModifier);
                        return bukkitModifier;
                    };
                    return bukkitModifierFunction;
                }));
    }
    
    
    public Set<NamespacedKey> getCraftingIngredientModifierKeys() {
        return nmsCraftingIngredientModfiers.keySet().stream()
                .map(CraftNamespacedKey::fromMinecraft)
                .collect(Collectors.toSet());
    }
    
    public Set<MinecraftKey> getNMSCraftingIngredientModifiersKeys() {
        return new HashSet<>(nmsCraftingIngredientModfiers.keySet());
    }
    
    public ExtendedCraftingIngredientModifier getCraftingIngredientModifier(MinecraftKey key, ItemStack ingredient) {
        return Optional.ofNullable(nmsCraftingIngredientModfiers.get(key))
                .map(f -> f.apply(ingredient))
                .orElse(null);
    }
    

    @Override
    public Iterator<IngredientModifier> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    //TODO remove this
    @Override
    public boolean registerModifier(NamespacedKey key, IngredientModifier modifier) {
        // TODO remove this
        return false;
    }

    @Override
    public void deregisterModifier(NamespacedKey key) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public CraftingIngredientModifier<CraftingIngredient, CraftingIngredient> getNBTModifier() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CraftingIngredientModifier<CraftingIngredient, CraftingIngredient> getCountModifier() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<? extends CraftingIngredientModifier<?, ?>> craftingIngredientModifierIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<? extends CraftingModifier<?, ?>> craftingRecipeModifierIterator() {
        // TODO Auto-generated method stub
        return null;
    }

}
