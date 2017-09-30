package com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.CRIngredient;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class CRChoiceIngredient extends CRIngredient<RecipeItemStack> implements ChoiceIngredient, NBTSerializable {

    public CRChoiceIngredient(RecipeItemStack nmsIngredient) {
        super(nmsIngredient);
    }

    public static CRChoiceIngredient deserialize(Map<String, Object> map) {
        RecipeItemStack nmsIngredient = NBTUtil.deserializeRecipeItemStack(NBTUtil.fromMap(map));
        return nmsIngredient == RecipeItemStack.a ? CREmptyIngredient.INSTANCE : new CRChoiceIngredient(nmsIngredient);
    }

    @Override
    public List<? extends ItemStack> getChoices() {
        if (nmsIngredient.choices == null) return List.of();

        return Arrays.stream(nmsIngredient.choices)
                .map(CraftItemStack::asCraftMirror)
                .collect(Collectors.toList());
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return NBTUtil.serializeRecipeItemStack(nmsIngredient);
    }


    @Override
    public Map<String, Object> serialize() {
        return NBTSerializable.super.serialize();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o instanceof CRChoiceIngredient) {
            CRChoiceIngredient that = (CRChoiceIngredient) o;
            net.minecraft.server.v1_12_R1.ItemStack[] thisChoices = nmsIngredient.choices;
            net.minecraft.server.v1_12_R1.ItemStack[] thatChoices = that.nmsIngredient.choices;

            if (thisChoices == null && thatChoices != null) return false;
            if (thisChoices != null && thatChoices == null) return false;
            if (thisChoices.length != thatChoices.length) return false;

            for (int i = 0; i < thisChoices.length; i++) {
                net.minecraft.server.v1_12_R1.ItemStack thisChoice = thisChoices[i];
                net.minecraft.server.v1_12_R1.ItemStack thatChoice = thatChoices[i];
                if (!net.minecraft.server.v1_12_R1.ItemStack.equals(thisChoice, thatChoice)) return false;
            }

            return true;

        } else if (o instanceof ChoiceIngredient) {
            return Objects.deepEquals(this.getChoices(), ((ChoiceIngredient) o).getChoices());

        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getChoices());
    }

}
