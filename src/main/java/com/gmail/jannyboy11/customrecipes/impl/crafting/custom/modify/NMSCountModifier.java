package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.CountIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.*;

import java.util.List;
import java.util.Objects;

public class NMSCountModifier extends NMSAbstractCraftingModifier<IRecipe, IRecipe> implements NBTSerializable {

    // -1 used as no count specified
    private final List<Integer> counts;

    public NMSCountModifier(List<Integer> counts) {
        this.counts = Objects.requireNonNull(counts);
    }

    public List<Integer> getCounts() {
        return counts;
    }

    @Override
    public IRecipe modify(IRecipe base) {
        NonNullList<RecipeItemStack> ingredients = base.d();

        IRecipe modified = new NMSProxyCraftingRecipe((inventoryCrafting, world) -> {
            if (counts.size() != ingredients.size()) return false;

            for (int i = 0; i < counts.size(); i++) {
                RecipeItemStack ingredient = ingredients.get(i);
                Integer count = counts.get(i);
                if (count == null) {
                    if (ingredient.choices == null) continue;
                    if (ingredient.choices.length == 0) continue;
                    return false;
                } else {
                    if (ingredient.choices == null) return false;
                    if (ingredient.choices.length == 0) return false;
                    if (ingredient.choices[0].getCount() == count.intValue()) return false;
                    continue;
                }
            }

            return base.a(inventoryCrafting, world);
        },
                base::craftItem,
                base::b,
                base::b,
                () -> {
                    NonNullList<RecipeItemStack> countSpecificIngredients = NonNullList.a();
                    for (int i = 0; i < ingredients.size(); i++) {
                        RecipeItemStack toAdd = ingredients.get(i);
                        if (toAdd.choices != null && toAdd.choices.length != 0) {
                            Integer count = counts.get(i);
                            if (count != null && count != -1) toAdd = new CountIngredient(toAdd, count).asNMSIngredient();
                        }
                        countSpecificIngredients.add(toAdd);
                    }
                    return countSpecificIngredients;
                },
                base::c,
                base::toBukkitRecipe,
                base::setKey);

        return modified;
    }

    @Override
    protected CraftingModifier createBukkitModifier() {
        return new CRCountModifier(this);
    }


    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound serialized = new NBTTagCompound();
        NBTTagList counts = new NBTTagList();
        for (Integer integer : this.counts) {
            counts.add(new NBTTagInt(integer == null ? -1 : integer.intValue()));
        }
        serialized.set("counts", counts);
        return serialized;
    }

}
