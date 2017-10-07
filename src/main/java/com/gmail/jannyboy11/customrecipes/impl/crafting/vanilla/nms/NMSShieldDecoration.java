package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.nms;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.recipe.CRShieldDecorationRecipe;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.RecipiesShield;
import net.minecraft.server.v1_12_R1.RecipiesShield.Decoration;

public class NMSShieldDecoration extends NMSShapelessRecipe<RecipiesShield.Decoration> {

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
