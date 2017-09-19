package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ArmorDyeRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSArmorDye;

import net.minecraft.server.v1_12_R1.RecipeArmorDye;

public class CRArmorDyeRecipe extends CRShapelessRecipe<RecipeArmorDye, NMSArmorDye> implements ArmorDyeRecipe {

    public CRArmorDyeRecipe(NMSArmorDye nmsRecipe) {
        super(nmsRecipe);
    }

    public CRArmorDyeRecipe(java.util.Map<String, ?> map) {
        super(map);
    }

}
