package com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.modify;

import com.gmail.jannyboy11.customrecipes.api.ingredient.Ingredient;
import com.gmail.jannyboy11.customrecipes.api.ingredient.modify.IngredientModifier;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import net.minecraft.server.v1_12_R1.ItemStack;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class CRAbstractIngredientModifier
        <PT extends Predicate<ItemStack>,
        PR extends Predicate<ItemStack>,
        N extends NMSIngredientModifier<PT, PR>>

        implements IngredientModifier {

    protected final N nmsModifier;

    public CRAbstractIngredientModifier(N nmsModifier) {
        this.nmsModifier = Objects.requireNonNull(nmsModifier);
    }

    @Override
    public Ingredient modify(Ingredient baseIngredient) {
        PT nmsPredicate = (PT) RecipeUtils.asNMSIngredient(baseIngredient);
        PR nmsModifiedIngredient = nmsModifier.modify(nmsPredicate);
        return bukkitStack -> nmsModifiedIngredient.test(CraftItemStack.asNMSCopy(bukkitStack));
    }

    public N getHandle() {
        return nmsModifier;
    }

}
