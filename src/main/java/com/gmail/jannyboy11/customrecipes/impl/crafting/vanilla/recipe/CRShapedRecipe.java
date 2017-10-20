package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.CraftingIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.ingredient.EmptyIngredient;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.Shape;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSShapedRecipe;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class CRShapedRecipe<V extends ShapedRecipes, S extends NMSShapedRecipe<V>> extends CRCraftingRecipe<V, S> implements ShapedRecipe {

    //built lazily from height, width and ingredients
    private Shape shape;
    
    public CRShapedRecipe(S nmsRecipe) {
        super(nmsRecipe);
    }

    @Override
    public int getWidth() {
        return nmsRecipe.getWidth();
    }

    @Override
    public int getHeight() {
        return nmsRecipe.getHeight();
    }

    @Override
    public String getGroup() {
       return nmsRecipe.getGroup();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ShapedRecipe)) return false;
        if (o instanceof CRShapedRecipe) return Objects.equals(this.nmsRecipe, ((CRShapedRecipe<?, ?>) o).nmsRecipe);

        ShapedRecipe that = (ShapedRecipe) o;

        return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getShape(), that.getShape()) &&
                Objects.equals(this.isHidden(), that.isHidden()) && Objects.equals(this.getGroup(), that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResult(), getShape(), isHidden(), getGroup());
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
            "result()=" + getResult() +
            ",shape()=" + getShape() +
            ",hidden()=" + isHidden() +
            ",group()=" + getGroup() +
            "}";
    }

    @Override
    public Shape getShape() {
        if (shape != null) return shape;
        
        final int height = getHeight();
        final int width = getWidth();
        char character = 'a';
        Iterator<? extends CraftingIngredient> ingredientIterator = this.getIngredients().iterator();
        
        String[] pattern = new String[height];
        Map<Character, CraftingIngredient> ingredientMap = new HashMap<>();
        
        for (int h = 0; h < height; h++) {
            StringBuilder row = new StringBuilder();
            
            for (int w = 0; w < width; w++) {
                char key = character++;
                
                CraftingIngredient ingredient = ingredientIterator.next();
                if (ingredient instanceof EmptyIngredient) {
                    key = ' ';
                }
                ingredientMap.put(key, ingredient);
                
                row.append(key);
            }
            
            pattern[h] = row.toString();
        }
        
        shape = new Shape(pattern, ingredientMap);
        
        return shape;
    }

}
