package com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.addremove;

import java.text.DecimalFormat;
import java.util.List;
import java.util.function.BiConsumer;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryCustom;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.gmail.jannyboy11.customrecipes.CustomRecipesPlugin;
import com.gmail.jannyboy11.customrecipes.api.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.impl.furnace.CRFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceManager;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.NMSFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.util.ReflectionUtil;

import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class FurnaceAdder implements BiConsumer<Player, List<String>> {

    private final CustomRecipesPlugin plugin;

    public FurnaceAdder(CustomRecipesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void accept(Player player, List<String> args) {
        org.bukkit.inventory.ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

        if (InventoryUtils.isEmptyStack(itemInMainHand)) {
            player.sendMessage(ChatColor.RED + "Put the result of the recipe in your main hand when executing this command.");
            return;
        }

        if (args.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Usage: /addrecipe furnace <key> [<xp>] [vanilla]");
            return;
        }

        String keyString = args.get(0);
        NamespacedKey bukkitKey = plugin.getKey(keyString);
        float xp = 0;
        if (args.size() >= 2) {
            try {
                xp = Float.parseFloat(args.get(1));
            } catch (NumberFormatException ignored) {
                //xp stays 0
            }
        }
        boolean vanilla = args.size() >= 3 ? "vanilla".equalsIgnoreCase(args.get(2)) : false;

        ItemStack result = CraftItemStack.asNMSCopy(itemInMainHand);

        player.openInventory(new FurnaceRecipeHolder(plugin, player, result, bukkitKey, xp, vanilla).getInventory());
    }

    public static class FurnaceRecipeHolder implements InventoryHolder, Listener {

        private final CustomRecipesPlugin plugin;
        private final Inventory hopperInventory;
        private final Player callbackPlayer;
        private final MinecraftKey key;
        private final ItemStack result;
        private final boolean vanilla;
        private final float xp;

        public FurnaceRecipeHolder(CustomRecipesPlugin plugin, Player player, ItemStack result, NamespacedKey key, float xp, boolean vanilla) {
            this.plugin = plugin;
            this.callbackPlayer = player;
            this.hopperInventory = plugin.getServer().createInventory(this, InventoryType.HOPPER, "Create a furnace recipe!");
            this.key = CraftNamespacedKey.toMinecraft(key);
            this.result = result;
            this.vanilla = vanilla;
            this.xp = xp;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }

        @Override
        public Inventory getInventory() {
            return hopperInventory;
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event) {
            if (event.getInventory().getHolder() instanceof FurnaceRecipeHolder) {
                FurnaceRecipeHolder holder = (FurnaceRecipeHolder) event.getInventory().getHolder();
                if (holder != this) return;

                Inventory inventory = event.getInventory();
                if (InventoryUtils.isEmpty(inventory)) {
                    holder.callbackPlayer.sendMessage(ChatColor.RED + "please put an ingredient int he inventory.");
                    HandlerList.unregisterAll(holder);
                    return;
                }

                org.bukkit.inventory.ItemStack ingredient = null;
                for (org.bukkit.inventory.ItemStack first : inventory) {
                    if (!InventoryUtils.isEmptyStack(first)) {
                        ingredient = first;
                        break;
                    }
                }

                CRFurnaceRecipe furnaceRecipe = new CRFurnaceRecipe(registerRecipe());
                String ingredientString = InventoryUtils.getItemName(ingredient);

                String recipeString = ingredientString + "" + ChatColor.RESET + " -> "
                        + InventoryUtils.getItemName(furnaceRecipe.getResult());
                if (holder.xp > 0) {
                    DecimalFormat decimalFormat = new DecimalFormat("##.##");
                    String xpString = decimalFormat.format(holder.xp);
                    recipeString += ChatColor.RESET + " (" + xpString + " xp)";
                }

                holder.callbackPlayer.sendMessage(String.format("%sAdded furnace recipe: %s%s%s!",
                        ChatColor.GREEN, ChatColor.WHITE, recipeString, ChatColor.WHITE));
                plugin.saveFurnaceRecipeFile(furnaceRecipe);

                HandlerList.unregisterAll(holder);
            }
        }

        private NMSFurnaceRecipe registerRecipe() {
            CraftInventoryCustom dispenserInventory = (CraftInventoryCustom) this.hopperInventory;
            IInventory minecraftInventory = (IInventory) ReflectionUtil.getDeclaredFieldValue(dispenserInventory, "inventory");
            NonNullList<ItemStack> itemStacks = (NonNullList<ItemStack>) ReflectionUtil.getDeclaredFieldValue(minecraftInventory, "items");
            NMSFurnaceManager recipesFurnace = NMSFurnaceManager.getInstance();

            ItemStack ingredientStack = itemStacks.stream().filter(nmsStack -> !nmsStack.isEmpty()).findFirst().get();
            RecipeItemStack ingredient = RecipeItemStack.a(new ItemStack[] {ingredientStack});            
            NMSFurnaceRecipe furnaceRecipe = new NMSFurnaceRecipe(key, ingredient, result, xp);

            if (vanilla) {
                recipesFurnace.addVanillaRecipe(furnaceRecipe);
            } else {
                recipesFurnace.addCustomRecipe(furnaceRecipe);
            }
            
            return furnaceRecipe;
        }

    }

}
