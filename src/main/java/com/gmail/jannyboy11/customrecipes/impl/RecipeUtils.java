package com.gmail.jannyboy11.customrecipes.impl;

import java.util.*;
import java.util.function.*;

import org.bukkit.inventory.CraftingInventory;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.modifier.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.crafting.*;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe.Bukkit2NMSCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.*;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.*;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.*;
import com.gmail.jannyboy11.customrecipes.impl.modifier.*;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.*;
public class RecipeUtils {

    private RecipeUtils() {}
    
    /**
     * Don't call this with a NMSCraftingRecipe pl0x! 
     * 
     * @param vanilla the real actual vanilla recipe
     * @return the CustomRecipes variant
     */
    private static CRCraftingRecipe<? extends IRecipe, ? extends NMSCraftingRecipe<? extends IRecipe>> getVanillaRecipe(IRecipe vanilla) {
        if (vanilla == null) return null;
        if (vanilla instanceof NMSCraftingRecipe) throw new IllegalArgumentException("Not a vanilla recipe!: " + vanilla);
        
        CRCraftingManager crCraftingManager = CustomRecipesPlugin.getInstance().getCraftingManager();
        
        MinecraftKey key = crCraftingManager.getKey(vanilla);
        if (key != null) {
            //get the NMSCrafting recipe the vanilla recipe was contained in
            NMSCraftingRecipe<?> nmsRecipe = crCraftingManager.getNMSRecipe(key);
            return (CRCraftingRecipe<?, ?>) nmsRecipe.getBukkitRecipe();
        }
        
        if (vanilla instanceof RecipeArmorDye) {
            return new CRArmorDyeRecipe(new NMSArmorDye((RecipeArmorDye) vanilla));
        } else if (vanilla instanceof RecipesBanner.AddRecipe) {
            return new CRBannerAddPatternRecipe(new NMSBannerAdd((RecipesBanner.AddRecipe) vanilla));
        } else if (vanilla instanceof RecipesBanner.DuplicateRecipe){
            return new CRBannerDuplicateRecipe(new NMSBannerDuplicate((RecipesBanner.DuplicateRecipe) vanilla));
        } else if (vanilla instanceof RecipeBookClone) {
            return new CRBookCloneRecipe(new NMSBookClone((RecipeBookClone) vanilla));
        } else if (vanilla instanceof RecipeFireworks) {
            return new CRFireworksRecipe(new NMSFireworks((RecipeFireworks) vanilla));
        } else if (vanilla instanceof RecipeMapClone) {
            return new CRMapCloneRecipe(new NMSMapClone((RecipeMapClone) vanilla));
        } else if (vanilla instanceof RecipeMapExtend) {
            return new CRMapExtendRecipe(new NMSMapExtend((RecipeMapExtend) vanilla));
        } else if (vanilla instanceof RecipeRepair) {
            return new CRRepairRecipe(new NMSRepair((RecipeRepair) vanilla));
        } else if (vanilla instanceof RecipiesShield.Decoration) {
            return new CRShieldDecorationRecipe(new NMSShieldDecoration((RecipiesShield.Decoration) vanilla));
        } else if (vanilla instanceof RecipeShulkerBox.Dye) {
            return new CRShulkerBoxDyeRecipe(new NMSShulkerBoxDye((RecipeShulkerBox.Dye) vanilla));
        } else if (vanilla instanceof RecipeTippedArrow) {
            return new CRTippedArrowRecipe(new NMSTippedArrow((RecipeTippedArrow) vanilla));
        } else if (vanilla instanceof ShapedRecipes) {
            return new CRShapedRecipe<>(new NMSShapedRecipe<>((ShapedRecipes) vanilla));
        } else if (vanilla instanceof ShapelessRecipes) {
            return new CRShapelessRecipe<>(new NMSShapelessRecipe<>((ShapelessRecipes) vanilla));
        } 

        throw new IllegalArgumentException("Not a vanilla recipe!: " + vanilla);
    }
    

    public static NMSCraftingRecipe<?> wrapVanilla(IRecipe vanilla) {
        if (vanilla instanceof NMSCraftingRecipe) {
            return (NMSCraftingRecipe<?>) vanilla;
        } else if (vanilla instanceof RecipeArmorDye) {
            return new NMSArmorDye((RecipeArmorDye) vanilla);
        } else if (vanilla instanceof RecipesBanner.AddRecipe) {
            return new NMSBannerAdd((RecipesBanner.AddRecipe) vanilla);
        } else if (vanilla instanceof RecipesBanner.DuplicateRecipe){
            return new NMSBannerDuplicate((RecipesBanner.DuplicateRecipe) vanilla);
        } else if (vanilla instanceof RecipeBookClone) {
            return new NMSBookClone((RecipeBookClone) vanilla);
        } else if (vanilla instanceof RecipeFireworks) {
            return new NMSFireworks((RecipeFireworks) vanilla);
        } else if (vanilla instanceof RecipeMapClone) {
            return new NMSMapClone((RecipeMapClone) vanilla);
        } else if (vanilla instanceof RecipeMapExtend) {
            return new NMSMapExtend((RecipeMapExtend) vanilla);
        } else if (vanilla instanceof RecipeRepair) {
            return new NMSRepair((RecipeRepair) vanilla);
        } else if (vanilla instanceof RecipiesShield.Decoration) {
            return new NMSShieldDecoration((RecipiesShield.Decoration) vanilla);
        } else if (vanilla instanceof RecipeShulkerBox.Dye) {
            return new NMSShulkerBoxDye((RecipeShulkerBox.Dye) vanilla);
        } else if (vanilla instanceof RecipeTippedArrow) {
            return new NMSTippedArrow((RecipeTippedArrow) vanilla);
        } else if (vanilla instanceof ShapedRecipes) {
            return new NMSShapedRecipe<>((ShapedRecipes) vanilla);
        } else if (vanilla instanceof ShapelessRecipes) {
            return new NMSShapelessRecipe<>((ShapelessRecipes) vanilla);
        }
        
        //TODO look in some registry (Map) for user-registered recipe implementation?
        throw new IllegalArgumentException("Unknown IRecipe implementation: " + vanilla);
    }


    
    public static NMSCraftingRecipe<?> getNMSRecipe(CraftingRecipe bukkit) {
        if (bukkit == null) return null;
        
        if (bukkit instanceof CRCraftingRecipe) {
            CRCraftingRecipe<?, ?> cr = (CRCraftingRecipe<?, ?>) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSCraftingRecipe(bukkit);
        }
    }
    
    public static CraftingRecipe getBukkitRecipe(IRecipe nms) {
        if (nms == null) return null;
        
        if (nms instanceof Bukkit2NMSCraftingRecipe) {
            Bukkit2NMSCraftingRecipe nmsified = (Bukkit2NMSCraftingRecipe) nms;
            return nmsified.getHandle();
        } else {
            //TODO call the other method?
            
            //TODO lots of if-else branching? caching in a map? static abuse all the way?
            //TODO shaped, shapeless, other vanilla recipes, as well as the modified recipes
            
            //TODO create a NMSCraftingRecipe class? which implements IRecipe and wraps an IRecipe?
            //TODO that way we could have the bukkit recipe as a field.
            
            
            //TODO fallback
            return null;
        }
    }
    
    public static <R extends IRecipe> CRCraftingRecipe getCRBukkitRecipe(R nms) {
        if (nms == null) return null;
        
        //TODO cases
        
        //fallBack TODO this is really dirty, can we fix this?
        return new CRCraftingRecipe<IRecipe, NMSCraftingRecipe<IRecipe>>(new NMSCraftingRecipe<IRecipe>(nms) {
            @Override
            public MinecraftKey getKey() {
                return null;
            }
        });
    }
    
    public static NMSFurnaceRecipe getNMSRecipe(FurnaceRecipe bukkit) {
        if (bukkit == null) return null;
        
        if (bukkit instanceof CRFurnaceRecipe) {
            CRFurnaceRecipe cr = (CRFurnaceRecipe) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSFurnaceRecipe(bukkit);
        }
    }
    
    public static FurnaceRecipe getBukkitRecipe(NMSFurnaceRecipe nms) {        
        if (nms == null) return null;
        
        return nms.getBukkitRecipe();
    }
    
    
    public static NMSCraftingModifier getNMSCrafringModifier(CraftingModifier bukkit) {
        if (bukkit == null) return null;
        
        if (bukkit instanceof CRCraftingModifier) {
            CRCraftingModifier cr = (CRCraftingModifier) bukkit;
            return cr.getHandle();
        } else {
            return new Bukkit2NMSCraftingModifier(bukkit);
        }
    }

    public static CraftingModifier getBukkitModifier(NMSCraftingModifier nms) {
        if (nms == null) return null;
        
        return nms.getBukkitModifier();
    }

    public static InventoryCrafting getNmsCraftingInventory(CraftingInventory bukkitInventory) {
        return (InventoryCrafting) ReflectionUtil.getDeclaredFieldValue(bukkitInventory, "inventory");
    }
    
    //TODO getBukkitCraftingInventory
    
}
