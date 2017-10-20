package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShieldDecorationRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RecipiesShield;
import net.minecraft.server.v1_12_R1.RecipiesShield.Decoration;

public class NMSShieldDecoration extends NMSCraftingRecipe<RecipiesShield.Decoration> {

    public static final MinecraftKey KEY = new MinecraftKey("shielddecoration");
    
    public MinecraftKey getKey() {
        return KEY;
    }
    
    public NMSShieldDecoration(Decoration delegate) {
        super(delegate);
    }
    
    @Override
    protected CRShieldDecorationRecipe createBukkitRecipe() {
        return new CRShieldDecorationRecipe(this);
    }
    
    @Override
    public ItemStack b() {
        return new ItemStack(Items.SHIELD);
    }

}
