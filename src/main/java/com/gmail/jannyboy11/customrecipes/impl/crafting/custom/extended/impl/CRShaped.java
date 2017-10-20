package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.Shape;

public class CRShaped extends CRCraftingRecipe<ExtendedShapedRecipe, NMSExtendedShapedRecipe> implements ShapedRecipe {

    public CRShaped(NMSExtendedShapedRecipe nmsRecipe) {
        super(nmsRecipe);
    }

    @Override
    public com.gmail.jannyboy11.customrecipes.api.crafting.recipe.Shape getShape() {
        Shape nmsShape = this.nmsRecipe.getShape();
        String[] pattern = nmsShape.getPattern();
        Map<Character, CraftingIngredient> ingredientMap = nmsShape.getIngredientMap().entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, e -> RecipeUtils.getBukkitCraftingRecipe(e.getValue())));
        
        return new com.gmail.jannyboy11.customrecipes.api.crafting.recipe.Shape(pattern, ingredientMap);
    }

}
