package com.gmail.jannyboy11.customrecipes.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = Objects.requireNonNull(itemStack).clone();
    }

    public ItemBuilder amount(int amount) {
        return change(i -> i.setAmount(amount));
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        return change(i -> i.addUnsafeEnchantment(enchantment, level));
    }

    public ItemBuilder data(MaterialData data) {
        return change(i -> i.setData(data));
    }

    /**
     * @deprecated data/damage values will be removed in MC 1.13, and are likely to be removed from the Bukkit API
     */
    @Deprecated(forRemoval = true)
    public ItemBuilder data(byte data) {
        return change(i -> i.getData().setData(data));
    }

    public ItemBuilder durability(short durability) {
        return change(i -> i.setDurability(durability));
    }

    public ItemBuilder type(Material type) {
        return change(i -> i.setType(type));
    }

    public ItemBuilder name(String displayName) {
        return changeMeta(meta -> meta.setDisplayName(displayName));
    }

    public ItemBuilder lore(List<String> lore) {
        return changeMeta(meta -> meta.setLore(lore));
    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }
    
    public ItemBuilder addLore(String line) {
        List<String> lore = itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore() ?
                new ArrayList<>(itemStack.getItemMeta().getLore()) : new ArrayList<>();
        lore.add(line);
        return lore(lore);
    }

    public ItemBuilder unbreakable() {
        return unbreakable(true);
    }

    public ItemBuilder unbreakable(boolean unbreakable) {
        return changeMeta(meta -> meta.setUnbreakable(unbreakable));
    }
    
    public ItemBuilder flags(ItemFlag... flags) {
        return changeMeta(meta -> meta.addItemFlags(flags));
    }

    @SuppressWarnings("unchecked")
    public <IM extends ItemMeta> ItemBuilder changeMeta(Consumer<IM> consumer) {
        return changeItemMeta(m -> consumer.accept((IM) m));
    }

    public ItemBuilder changeItemMeta(Consumer<? super ItemMeta> consumer) {
        return change(i -> {
            ItemMeta meta = i.getItemMeta();
            consumer.accept(meta);
            i.setItemMeta(meta);
        });
    }

    public ItemBuilder change(Consumer<? super ItemStack> consumer) {
        ItemBuilder builder = new ItemBuilder(itemStack);
        consumer.accept(builder.itemStack);
        return builder;
    }

    public ItemBuilder apply(Function<? super ItemStack, ? extends ItemStack> function) {
        return new ItemBuilder(function.apply(build()));
    }

    public ItemStack build() {
        return itemStack.clone();
    }
}