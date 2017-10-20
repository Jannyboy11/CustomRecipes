package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.Map;
import java.util.Objects;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapelessRecipes;

public class CRShapelessRecipe<V extends ShapelessRecipes, S extends NMSShapelessRecipe<V>> extends CRCraftingRecipe<V, S> implements ShapelessRecipe {

    public CRShapelessRecipe(S nmsRecipe) {
        super(nmsRecipe);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ShapelessRecipe)) return false;
        if (o instanceof CRShapelessRecipe) return Objects.equals(this.nmsRecipe, ((CRShapelessRecipe<?, ?>) o).nmsRecipe);

        ShapelessRecipe that = (ShapelessRecipe) o;

        return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getIngredients(), that.getIngredients()) &&
                Objects.equals(this.isHidden(), that.isHidden()) && Objects.equals(this.getGroup(), that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResult(), getIngredients(), isHidden(), getGroup());
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
            "result()=" + getResult() +
            ",ingredients()=" + getIngredients() +
            ",hidden()=" + isHidden() +
            ",group()=" + getGroup() +
            "}";
    }

}
