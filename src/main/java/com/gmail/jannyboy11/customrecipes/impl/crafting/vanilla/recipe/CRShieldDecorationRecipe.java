package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShieldDecorationRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSShieldDecoration;

import net.minecraft.server.v1_12_R1.RecipiesShield;

public class CRShieldDecorationRecipe extends CRShapelessRecipe<RecipiesShield.Decoration, NMSShieldDecoration> implements ShieldDecorationRecipe {

    public CRShieldDecorationRecipe(NMSShieldDecoration nmsRecipe) {
        super(nmsRecipe);
    }

    public CRShieldDecorationRecipe(java.util.Map<String, ?> map) {
        super(map);
    }

}
