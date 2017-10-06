package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe;

import java.util.Map;
import java.util.Objects;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.ShapedRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.CRCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSShapedRecipe;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;
import net.minecraft.server.v1_12_R1.ShapedRecipes;

public class CRShapedRecipe<V extends ShapedRecipes, S extends NMSShapedRecipe<V>> extends CRCraftingRecipe<V, S> implements ShapedRecipe {

    public CRShapedRecipe(S nmsRecipe) {
        super(nmsRecipe);
    }

    public CRShapedRecipe(NBTTagCompound recipeCompound) {
        this((S) new NMSShapedRecipe((V) deserializeNmsRecipe(recipeCompound)));
    }

    public CRShapedRecipe(Map<String, ?> map) {
        this(NBTUtil.fromMap(map));
    }

    public NBTTagCompound serializeToNbt() {
        NBTTagCompound serialized = new NBTTagCompound();
        serialized.set("key", NBTUtil.serializeKey(getHandle().getKey()));
        serialized.set("result", NBTUtil.serializeItemStack(getHandle().b()));
        if (hasGroup()) serialized.setString("group", getGroup());
        serialized.setInt("width", getWidth());
        serialized.setInt("height", getHeight());
        NBTTagList ingredients = new NBTTagList();
        for (RecipeItemStack ingr : nmsRecipe.d()) {
            ingredients.add(NBTUtil.serializeRecipeItemStack(ingr));
        }
        serialized.set("ingredients", ingredients);
        return serialized;
    }

    //TODO change return type to NMSCraftingRecipe? or even better, refactor this functionality to NMSCraftingRecipe?
    protected static ShapedRecipes deserializeNmsRecipe(NBTTagCompound recipeCompound) {
        String group = recipeCompound.hasKeyOfType("group", NBTUtil.STRING) ? recipeCompound.getString("group") : "";
        int width = recipeCompound.getInt("width");
        int height = recipeCompound.getInt("height");
        NonNullList<RecipeItemStack> ingredients = NonNullList.a();
        NBTTagList nbtIngredients = recipeCompound.getList("ingredients", NBTUtil.COMPOUND);
        for (int i = 0; i < nbtIngredients.size(); i++) {
            NBTTagCompound ingredientTag = nbtIngredients.get(i);
            RecipeItemStack recipeItemStack = NBTUtil.deserializeRecipeItemStack(ingredientTag);
            ingredients.add(recipeItemStack);
        }
        NBTTagCompound resultCompound = (NBTTagCompound) recipeCompound.get("result");
        ItemStack result = new ItemStack(resultCompound);
        ShapedRecipes shapedRecipes = new ShapedRecipes(group, width, height, ingredients, result);
        if (recipeCompound.hasKey("key")) {
            shapedRecipes.setKey(NBTUtil.deserializeKey(recipeCompound.getCompound("key")));
        }
        return shapedRecipes;
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

        return Objects.equals(this.getResult(), that.getResult()) && Objects.equals(this.getIngredients(), that.getIngredients()) &&
                Objects.equals(this.getWidth(), that.getWidth()) && Objects.equals(this.getHeight(), that.getHeight()) &&
                Objects.equals(this.isHidden(), that.isHidden()) && Objects.equals(this.getGroup(), that.getGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResult(), getWidth(), getHeight(), getIngredients(), isHidden(), getGroup());
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
            "result()=" + getResult() +
            ",width()=" + getWidth() +
            ",heigth()=" + getHeight() +
            ",ingredients()=" + getIngredients() +
            ",hidden()=" + isHidden() +
            ",group()=" + getGroup() +
            "}";
    }

}
