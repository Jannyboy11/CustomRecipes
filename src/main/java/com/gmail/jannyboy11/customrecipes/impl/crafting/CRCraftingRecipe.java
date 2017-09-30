package com.gmail.jannyboy11.customrecipes.impl.crafting;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gmail.jannyboy11.customrecipes.impl.ingredient.CRIngredient;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.CraftingInventory;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.RecipeUtils;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.NMSCraftingRecipe;
import net.minecraft.server.v1_12_R1.IRecipe;
import net.minecraft.server.v1_12_R1.InventoryCrafting;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.WorldServer;

public class CRCraftingRecipe<V extends IRecipe, R extends NMSCraftingRecipe<V>> implements CraftingRecipe {

    protected final R nmsRecipe;

    public CRCraftingRecipe(R nmsRecipe) {
        this.nmsRecipe = Objects.requireNonNull(nmsRecipe);
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        CraftWorld cWorld = (CraftWorld) world;

        WorldServer nmsWorld = cWorld.getHandle();
        InventoryCrafting nmsCraftingInventory = RecipeUtils.getNmsCraftingInventory(craftingInventory);

        return nmsRecipe.a(nmsCraftingInventory, nmsWorld);
    }

    @Override
    public CraftItemStack craftItem(CraftingInventory craftingInventory) {
        InventoryCrafting nmsCraftingInventory = RecipeUtils.getNmsCraftingInventory(craftingInventory);
        return CraftItemStack.asCraftMirror(nmsRecipe.craftItem(nmsCraftingInventory));
    }

    @Override
    public CraftItemStack getResult() {
        return CraftItemStack.asCraftMirror(nmsRecipe.b());
    }

    @Override
    public boolean isHidden() {
        return nmsRecipe.c();
    }

    @Override
    public List<CRChoiceIngredient> getIngredients() {
        return nmsRecipe.d().stream()
                .map(RecipeUtils::asBukkitIngredient)
                .collect(Collectors.toList());
    }

    @Override
    public List<CraftItemStack> getLeftOverItems(CraftingInventory craftingInventory) {
        InventoryCrafting nmsCraftingInventory = RecipeUtils.getNmsCraftingInventory(craftingInventory);
        return nmsRecipe.b(nmsCraftingInventory).stream()
                .map(CraftItemStack::asCraftMirror)
                .collect(Collectors.toList());
    }

    @Override
    public final NamespacedKey getKey() {
        MinecraftKey mcKey = nmsRecipe.getKey();
        return mcKey == null ? null : CraftNamespacedKey.fromMinecraft(mcKey);
    }

    public NMSCraftingRecipe<V> getHandle() {
        return nmsRecipe;
    }


    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof CRCraftingRecipe)) return false;
        @SuppressWarnings("rawtypes")
        CRCraftingRecipe that = (CRCraftingRecipe) object;
        return this.nmsRecipe.equals(that.nmsRecipe);
    }

    @Override
    public int hashCode() {
        return nmsRecipe.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "key()=" + getKey() +
                ",result()=" + getResult() +
                ",hidden()=" + isHidden() +
                ",ingredients()=" + getIngredients() +
                "}";
    }

}
