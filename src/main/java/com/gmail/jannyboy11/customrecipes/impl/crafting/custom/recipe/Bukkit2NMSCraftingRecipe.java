package com.gmail.jannyboy11.customrecipes.impl.crafting.custom.recipe;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.*;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.crafting.CraftingRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.*;
import com.gmail.jannyboy11.customrecipes.api.ingredient.ChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.crafting.*;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms.*;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.*;
import com.gmail.jannyboy11.customrecipes.serialize.NBTSerializable;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.*;

public class Bukkit2NMSCraftingRecipe extends NMSCraftingRecipe<IRecipe> implements NBTSerializable {

    //dirty hacks!
    private static final String CUSTOM_RECIPES_BUKKITRECIPE_KEY = "cr-bukkit2nmsrecipe-";
    private static final AtomicInteger keyCount = new AtomicInteger(0);

    private static NamespacedKey newNamespacedKey() {
        CustomRecipesPlugin plugin = CustomRecipesPlugin.getInstance();
        return new NamespacedKey(plugin, CUSTOM_RECIPES_BUKKITRECIPE_KEY + keyCount.getAndIncrement() + UUID.randomUUID().toString());
    }

    public Bukkit2NMSCraftingRecipe(CraftingRecipe bukkitRecipe) {
        this.bukkit = Objects.requireNonNull(bukkitRecipe);
    }
    
    public Bukkit2NMSCraftingRecipe(Map<String, Object> map) {
        this((CraftingRecipe) map.get("bukkit"));
    }
    
    @Override
    public Map<String, Object> serialize() {
        return Collections.singletonMap("bukkit", bukkit);
    }

    @Override
    public MinecraftKey getKey() {
        NamespacedKey key = bukkit.getKey();
        return key == null ? null : CraftNamespacedKey.toMinecraft(key);
    }

    @Override
    public NBTTagCompound serializeToNbt() {
        return NBTUtil.fromMap(serialize());
    }


    public static CraftInventoryCrafting getBukkitCraftingInventory(InventoryCrafting inventoryCrafting) {
        IInventory resultInventory = inventoryCrafting.resultInventory;
        return new CraftInventoryCrafting(inventoryCrafting, resultInventory);
    }

    @Override
    public boolean a(InventoryCrafting inventoryCrafting, World world) {
        CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);
        CraftWorld bukkitWorld = world.getWorld();		

        return bukkit.matches(bukkitInventory, bukkitWorld);
    }

    @Override
    public ItemStack b() {
        return CraftItemStack.asNMSCopy(bukkit.getResult());
    }

    @Override
    public NonNullList<RecipeItemStack> d() {
        return bukkit.getIngredients().stream()
                .map(CRCraftingIngredient::asNMSIngredient)
                .collect(Collectors.toCollection(NonNullList::a));
    }

    @Override
    public NonNullList<ItemStack> b(InventoryCrafting inventoryCrafting) {
        CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);

        List<? extends org.bukkit.inventory.ItemStack> bukkitLeftovers = bukkit.getLeftOverItems(bukkitInventory);
        ItemStack[] nmsLeftovers = bukkitLeftovers.stream().map(CraftItemStack::asNMSCopy).toArray(size -> new ItemStack[size]);
        return NonNullList.a(ItemStack.a, nmsLeftovers);
    }

    @Override
    public ItemStack craftItem(InventoryCrafting inventoryCrafting) {
        CraftInventoryCrafting bukkitInventory = getBukkitCraftingInventory(inventoryCrafting);

        return CraftItemStack.asNMSCopy(bukkit.craftItem(bukkitInventory));
    }

    @Override
    public boolean c() {
        return bukkit.isHidden();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;
        if (!(object instanceof Bukkit2NMSCraftingRecipe)) return false;

        Bukkit2NMSCraftingRecipe that = (Bukkit2NMSCraftingRecipe) object;
        return this.bukkit.equals(that.bukkit);
    }

    @Override
    public int hashCode() {
        return bukkit.hashCode();
    }	

    @Override
    public String toString() {
        return getClass().getName() + "{bukkit=" + bukkit + "}";
    }

    @Override
    public CraftingRecipe getBukkitRecipe() {
        return bukkit;
    }


    @SuppressWarnings("rawtypes")
    @Override
    public Recipe toBukkitRecipe() {
        //we try the best we can to do something meaningful.

        if (bukkit instanceof CRShapedRecipe) {
            return ((CRShapedRecipe) bukkit).getHandle().toBukkitRecipe();

        } else if (bukkit instanceof CRShapelessRecipe) {
            return ((CRShapelessRecipe) bukkit).getHandle().toBukkitRecipe();


        } else if (bukkit instanceof ShapedRecipe) {
            //try our very best to do something useful
            ShapedRecipe shapedRecipe = (ShapedRecipe) bukkit;
            NamespacedKey namespacedKey = CustomRecipesPlugin.getInstance().getCraftingManager().getKey(shapedRecipe);
            if (namespacedKey == null) namespacedKey = newNamespacedKey();

            org.bukkit.inventory.ShapedRecipe bukkitShapedRecipe = new org.bukkit.inventory.ShapedRecipe(namespacedKey, bukkit.getResult());

            AtomicInteger character = new AtomicInteger('a');
            List<StringBuilder> shapeBuilder = new ArrayList<>();
            for (int i = 0; i < shapedRecipe.getHeight(); i++) {
                shapeBuilder.add(new StringBuilder());
                for (int j = 0; j < shapedRecipe.getWidth(); j++) {
                    shapeBuilder.get(i).append((char) character.getAndIncrement());
                }
            }

            String[] shape = shapeBuilder.stream().map(StringBuilder::toString).toArray(size -> new String[size]);
            bukkitShapedRecipe.shape(shape);
            // for clarity. arrayNum goes from top to bottom, charNum goes from left to right
            /* [a,b,c],
             * [d,e,f],
             * [g,h,i]
             */

            int arrayNum = 0;
            int charNum = 0;
            for (ChoiceIngredient ingr : shapedRecipe.getIngredients()) {
                Iterator<? extends org.bukkit.inventory.ItemStack> choiceIterator = ingr.getChoices().iterator();
                if (choiceIterator.hasNext()) {
                    char key = shape[arrayNum].charAt(charNum);
                    bukkitShapedRecipe.setIngredient(key, choiceIterator.next().getType());
                }
                //skip the other choices since they would take up the same item slot.
                //instead, the materialdata is ignored for this recipe, such that multiple data values would still work.
                //this seems like the most reasonable thing to do as we don't know what the choices are.

                if (++charNum > shape[arrayNum].length()) {
                    charNum = 0;
                    arrayNum++;
                }
            }

            return bukkitShapedRecipe;

        } else if (bukkit instanceof ShapelessRecipe) {
            //try our very best to do something useful
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) bukkit;
            NamespacedKey namespacedKey = CustomRecipesPlugin.getInstance().getCraftingManager().getKey(shapelessRecipe);
            if (namespacedKey == null) namespacedKey = newNamespacedKey();

            org.bukkit.inventory.ShapelessRecipe bukkitShapelessRecipe = new org.bukkit.inventory.ShapelessRecipe(namespacedKey, bukkit.getResult());

            for (ChoiceIngredient ingr : shapelessRecipe.getIngredients()) {
                Iterator<? extends org.bukkit.inventory.ItemStack> choiceIterator = ingr.getChoices().iterator();
                if (choiceIterator.hasNext()) {
                    bukkitShapelessRecipe.addIngredient(choiceIterator.next().getType());
                }
                //skip the other choices since they would take up the same item slot.
                //instead, the materialdata is ignored for this recipe, such that multiple data values would still work.
                //this seems like the most reasonable thing to do as we don't know what the choices are.
            }

            return bukkitShapelessRecipe;
        }

        //Recipe was neither a Shaped nor a Shapeless recipe, return the generic crafting recipe.
        //Bukkit and other plugins will not like this, but it's the best we can do instead of returning null.
        return bukkit;
    }

   
    @Override
    public void setKey(MinecraftKey key) {		
        //The recipe already has a key m8!
    }

}
