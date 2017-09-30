package com.gmail.jannyboy11.customrecipes.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;

import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.FixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify.CRCraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify.NMSCraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.NMSFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.CRIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.Bukkit2NMSIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla.CREmptyIngredient;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.CraftingInventory;

import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.*;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify.Bukkit2NMSCraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.*;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.*;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.*;

public class RecipeUtils {

    private RecipeUtils() {}

    private static Map<Class<? extends IRecipe>, Class<? extends NMSCraftingRecipe>> nmsCraftingRecipeMappings = new HashMap<>();
    static {
        registerNMSCraftingRecipe(RecipeArmorDye.class, NMSArmorDye.class);
        registerNMSCraftingRecipe(RecipesBanner.AddRecipe.class, NMSBannerAdd.class);
        registerNMSCraftingRecipe(RecipeBookClone.class, NMSBookClone.class);
        registerNMSCraftingRecipe(RecipeFireworks.class, NMSFireworks.class);
        registerNMSCraftingRecipe(RecipeMapClone.class, NMSMapClone.class);
        registerNMSCraftingRecipe(RecipeMapExtend.class, NMSMapExtend.class);
        registerNMSCraftingRecipe(RecipeRepair.class, NMSRepair.class);
        registerNMSCraftingRecipe(RecipiesShield.Decoration.class, NMSShieldDecoration.class);
        registerNMSCraftingRecipe(RecipeShulkerBox.Dye.class, NMSShulkerBoxDye.class);
        registerNMSCraftingRecipe(RecipeTippedArrow.class, NMSTippedArrow.class);
        //TODO why don't shaped and shapeless recipes work with registerNMSCraftingRecipe?
        nmsCraftingRecipeMappings.put(ShapedRecipes.class, NMSShapedRecipe.class);
        nmsCraftingRecipeMappings.put(ShapelessRecipes.class, NMSShapelessRecipe.class);
    }

    public static <V extends IRecipe> void registerNMSCraftingRecipe(Class<? extends V> vanilla, Class<? extends NMSCraftingRecipe<? extends V>> nms) {
        nmsCraftingRecipeMappings.put(vanilla, nms);
    }


    public static CraftingRecipe getBukkitRecipe(final IRecipe vanilla) {
        if (vanilla == null) return null;

        return getNMSRecipe(vanilla).getBukkitRecipe();
    }

    public static NMSCraftingRecipe<?> getNMSRecipe(IRecipe nms) {
        if (nms == null) return null;
        if (nms instanceof NMSCraftingRecipe) return (NMSCraftingRecipe) nms;

        //TODO support other NMS implementations?

        //try to get from the mappings
        Class vanillaClazz = nms.getClass();
        Class<? extends NMSCraftingRecipe> nmsClazz = nmsCraftingRecipeMappings.get(vanillaClazz);
        while (nmsClazz == null && IRecipe.class.isAssignableFrom(vanillaClazz)) {
            vanillaClazz = vanillaClazz.getSuperclass();
            nmsClazz = nmsCraftingRecipeMappings.get(vanillaClazz);
        }
        if (nmsClazz != null) {
            try {
                Constructor<? extends NMSCraftingRecipe> nmsConstructor = nmsClazz.getConstructor(vanillaClazz);
                NMSCraftingRecipe nmsRecipe = nmsConstructor.newInstance(nms);

                return nmsRecipe;
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new RuntimeException("There should be a NMSCraftingRecipe registered for IRecipe implementation " + nms.getClass() +
                        " with a public single argument constructor with parameter type " + nms.getClass(), e);
            }
        }

        //nothing we could do :(
        throw new IllegalStateException("No NMSCraftingRecipe mapping found for recipe implementation: " + nms.getClass());
    }

    
    public static NMSCraftingRecipe<?> getNMSRecipe(final CraftingRecipe bukkit) {
        if (bukkit == null) return null;
        
        if (bukkit instanceof CRCraftingRecipe) {
            CRCraftingRecipe<?, ?> cr = (CRCraftingRecipe<?, ?>) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSCraftingRecipe(bukkit);
        }
    }

    public static NMSFixedFurnaceRecipe getNMSRecipe(final FixedFurnaceRecipe bukkit) {
        if (bukkit == null) return null;

        if (bukkit instanceof CRFixedFurnaceRecipe) {
            CRFixedFurnaceRecipe cr = (CRFixedFurnaceRecipe) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSFixedFurnaceRecipe(bukkit);
        }
    }
    
    public static NMSFurnaceRecipe getNMSRecipe(final FurnaceRecipe bukkit) {
        if (bukkit == null) return null;
        
        if (bukkit instanceof CRFurnaceRecipe) {
            CRFurnaceRecipe cr = (CRFurnaceRecipe) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSFurnaceRecipe(bukkit);
        }
    }
    
    public static FurnaceRecipe getBukkitRecipe(final NMSFurnaceRecipe nms) {
        if (nms == null) return null;
        
        return nms.getBukkitRecipe();
    }
    
    
    public static NMSCraftingModifier getNMSCrafringModifier(final CraftingModifier bukkit) {
        if (bukkit == null) return null;
        
        if (bukkit instanceof CRCraftingModifier) {
            CRCraftingModifier cr = (CRCraftingModifier) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSCraftingModifier(bukkit);
        }
    }

    public static CraftingModifier getBukkitModifier(final NMSCraftingModifier nms) {
        if (nms == null) return null;
        
        return nms.getBukkitModifier();
    }


    public static InventoryCrafting getNmsCraftingInventory(final CraftingInventory bukkitInventory) {
        return (InventoryCrafting) ReflectionUtil.getDeclaredFieldValue(bukkitInventory, "inventory");
    }

    public static CraftInventoryCrafting getBukkitCraftingInventory(InventoryCrafting inventoryCrafting) {
        IInventory resultInventory = inventoryCrafting.resultInventory;
        return new CraftInventoryCrafting(inventoryCrafting, resultInventory);
    }


    public static CRChoiceIngredient asBukkitIngredient(RecipeItemStack ingredient) {
        //TODO can we handle subclasses of RecipeItemStack?
        //TODO Right now, we can't because the subclass doesn't have a toBukkit we can override. But this should be a thing.
        //TODO adjust the RecipeItemStackInjected class
        return ingredient == RecipeItemStack.a ? CREmptyIngredient.INSTANCE : new CRChoiceIngredient(ingredient);
    }

    public static RecipeItemStack asNMSIngredient(ChoiceIngredient ingredient) {
        if (ingredient instanceof CRChoiceIngredient) {
            CRChoiceIngredient crIngredient = (CRChoiceIngredient) ingredient;
            return crIngredient.getHandle();
        }

        Predicate<? super ItemStack> nmsIngredient = asNMSIngredient((Ingredient) ingredient);
        RecipeItemStack recipeItemStack = nmsIngredient instanceof RecipeItemStack ?
                (RecipeItemStack) nmsIngredient :
                new InjectedIngredient(nmsIngredient).asNMSIngredient();

        ItemStack[] choices = ingredient.getChoices().stream()
                .map(CraftItemStack::asNMSCopy)
                .toArray(ItemStack[]::new);
        ReflectionUtil.setFinalFieldValue(recipeItemStack, "choices", choices);

        return recipeItemStack;
    }

    public static Predicate<? super ItemStack> asNMSIngredient(Ingredient ingredient) {
        if (ingredient instanceof ChoiceIngredient) {
            ChoiceIngredient choiceIngredient = (ChoiceIngredient) ingredient;
            return asNMSIngredient(choiceIngredient);
        }

        if (ingredient instanceof CRIngredient) {
            CRIngredient<RecipeItemStack> crIngredient = (CRIngredient<RecipeItemStack>) ingredient;
            return crIngredient.getHandle();
        }

        //fallback
        return new Bukkit2NMSIngredient(ingredient);
    }

    public static RecipeItemStack asRecipeItemStack(Ingredient ingredient) {
        Predicate<? super ItemStack> nmsIngredient = asNMSIngredient(ingredient);

        if (nmsIngredient instanceof RecipeItemStack) {
            RecipeItemStack recipeItemStack = (RecipeItemStack) nmsIngredient;
            return recipeItemStack;
        }

        return new InjectedIngredient(nmsIngredient).asNMSIngredient();
    }
    
}
