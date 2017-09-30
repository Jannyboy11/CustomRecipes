package com.gmail.jannyboy11.customrecipes.api.ingredient.modify;

/**
 * Modifier that restricts the ingredient to only accept items with a certain damage value.
 *
 * @deprecated data values will be removed in Minecraft 1.13. From then, use the {@link NBTModifier} instead.
 */
//TODO I should make an NBT modifier which only checks for subtag equality when 1.13 comes out.
@Deprecated(since = "3.0.0", forRemoval = true)
public interface DamageModifier extends IngredientModifier {

    public int getDamage();

}

