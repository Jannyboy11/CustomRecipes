package com.gmail.jannyboy11.customrecipes.impl.modify.nms;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.gmail.jannyboy11.customrecipes.impl.crafting.custom.extended.ExtendedCraftingIngredient;

import net.minecraft.server.v1_12_R1.ItemStack;

public class ExtendedCraftingIngredientWrapper implements ExtendedCraftingIngredient {
    
    private final ExtendedCraftingIngredient base;
    
    private Predicate<? super ItemStack> andPredicate;
    private Function<? super ItemStack, ? extends ItemStack> replaceRemainder;
    private Supplier<? extends Optional<ItemStack>> replaceFirstSupplier;
    
    protected ExtendedCraftingIngredientWrapper(ExtendedCraftingIngredient wrapped) {
        this.base = wrapped;
    }
    
    public ExtendedCraftingIngredientWrapper(ExtendedCraftingIngredient base, Predicate<? super ItemStack> inputPredicate) {
        this(base);
        this.andPredicate = inputPredicate;
    }
    
    public ExtendedCraftingIngredientWrapper(ExtendedCraftingIngredient base, Predicate<? super ItemStack> inputPredicate,
            Function<? super ItemStack, ? extends ItemStack> replaceRemainder) {
        this(base, inputPredicate);
        this.replaceRemainder = replaceRemainder;
    }
    
    public ExtendedCraftingIngredientWrapper(ExtendedCraftingIngredient base, Predicate<? super ItemStack> inputPredicate,
            Function<? super ItemStack, ? extends ItemStack> replaceRemainder, Supplier<? extends Optional<ItemStack>> replaceFirstSupplier) {
        this(base, inputPredicate, replaceRemainder);
        this.replaceFirstSupplier = replaceFirstSupplier;
    }
    
    

    @Override
    public boolean accepts(ItemStack input) {
        if (andPredicate != null && !andPredicate.test(input)) return false; 
        
        return base.accepts(input);
    }
    
    public Optional<ItemStack> firstItemStack() {
        if (replaceFirstSupplier != null) return replaceFirstSupplier.get();
        
        return base.firstItemStack();
    }
    
    public ItemStack getRemainder(ItemStack ingredient) {
        if (replaceRemainder != null) return replaceRemainder.apply(ingredient);
        
        return base.getRemainder(ingredient);
    }

}
