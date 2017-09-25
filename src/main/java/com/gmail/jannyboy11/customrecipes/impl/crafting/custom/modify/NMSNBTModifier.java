package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.crafting.modify.CraftingModifier;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.NBTIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import net.minecraft.server.v1_12_R1.*;

import java.util.List;
import java.util.Objects;

public class NMSNBTModifier extends NMSAbstractCraftingModifier<IRecipe, IRecipe> implements NBTSerializable {

    private final List<NBTTagCompound> tags; //can contain null values

    public NMSNBTModifier(List<NBTTagCompound> tags) {
        this.tags = Objects.requireNonNull(tags);
    }

    @Override
    protected CraftingModifier createBukkitModifier() {
        return new CRNBTModifier(this);
    }

    @Override
    public IRecipe modify(IRecipe base) {
        NonNullList<RecipeItemStack> ingredients = base.d();

        IRecipe modified = new NMSProxyCraftingRecipe((inventoryCrafting, world) -> {
            if (tags.size() != ingredients.size()) return false;

            for (int i = 0; i < tags.size(); i++) {
                RecipeItemStack ingredient = ingredients.get(i);
                NBTTagCompound tag = tags.get(i);
                if (tag == null) {
                    if (ingredient.choices == null) continue;
                    if (ingredient.choices.length == 0) continue;
                    return false;
                } else {
                    if (ingredient.choices == null) return false;
                    if (ingredient.choices.length == 0) return false;
                    if (!Objects.equals(ingredient.choices[0].getTag(), tag)) return false;
                    continue;
                }
            }

            return base.a(inventoryCrafting, world);
        },
                base::craftItem,
                base::b,
                base::b,
                () -> {
                    NonNullList<RecipeItemStack> nbtSpecificIngredients = NonNullList.a();
                    for (int i = 0; i < ingredients.size(); i++) {
                        RecipeItemStack toAdd = ingredients.get(i);
                        if (toAdd.choices != null && toAdd.choices.length != 0) {
                            toAdd = new NBTIngredient(toAdd, tags.get(i)).asNMSIngredient();
                        }
                        nbtSpecificIngredients.add(toAdd);
                    }
                    return nbtSpecificIngredients;
                },
                base::c,
                base::toBukkitRecipe,
                base::setKey);

        return modified;
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        NBTTagCompound serialized = new NBTTagCompound();
        NBTTagList nbtTagList = new NBTTagList();
        for (NBTTagCompound tag : tags) {
            nbtTagList.add(tag == null ? new NBTTagCompound() : tag);
        }
        serialized.set("tags", nbtTagList);
        return serialized;
    }




}
