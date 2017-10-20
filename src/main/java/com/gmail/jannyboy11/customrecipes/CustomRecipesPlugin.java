package com.gmail.jannyboy11.customrecipes;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.logging.Level;
import java.util.stream.*;

import com.gmail.jannyboy11.customrecipes.commands.ManageRecipesCommandExecutor;
import com.gmail.jannyboy11.customrecipes.gui.framework.GuiOpenListener;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.custom.InjectedIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla.CRChoiceIngredient;
import com.gmail.jannyboy11.customrecipes.impl.ingredient.vanilla.CREmptyIngredient;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftNamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.jannyboy11.customrecipes.api.*;
import com.gmail.jannyboy11.customrecipes.api.crafting.*;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.*;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.simple.SimpleShapedRecipe;
import com.gmail.jannyboy11.customrecipes.api.crafting.recipe.simple.SimpleShapelessRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.FurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.FixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.furnace.recipe.simple.SimpleFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.api.ingredient.*;
import com.gmail.jannyboy11.customrecipes.api.ingredient.SimpleChoiceIngredient.SimpleEmptyIngredient;
import com.gmail.jannyboy11.customrecipes.api.util.InventoryUtils;
import com.gmail.jannyboy11.customrecipes.impl.crafting.*;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.extended.MojangCraftingRecipe;
import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.extended.MojangRecipeDeserializer;
import com.gmail.jannyboy11.customrecipes.impl.furnace.*;
import com.gmail.jannyboy11.customrecipes.impl.furnace.custom.*;
import com.gmail.jannyboy11.customrecipes.impl.furnace.vanilla.NMSFixedFurnaceRecipe;
import com.gmail.jannyboy11.customrecipes.serialize.*;
import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.*;
import net.minecraft.server.v1_12_R1.CraftingManager;

/**
 * The CustomRecipes Plugin's main class. Internal use only!
 * 
 * @author Jan
 */
//TODO lots of cleaning up :D
public class CustomRecipesPlugin extends JavaPlugin implements CustomRecipesApi {

    //Hacks!
    private static CustomRecipesPlugin instance;

    private final Map<String, BiConsumer<? super NBTSerializable, ? super File>> writers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, Function<? super File, ? extends Recipe>> readers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private CRCraftingManager craftingManager;
    private CRFurnaceManager furnaceManager;

    private Set<MinecraftKey> vanillaCraftingRecipes;
    private Map<net.minecraft.server.v1_12_R1.ItemStack, net.minecraft.server.v1_12_R1.ItemStack> vanillaFurnaceRecipes;



    private void recordVanillaRecipes() {
        getServer().resetRecipes(); //Is called before onEnable of other plugins, so shouldn't cause trouble.
        vanillaCraftingRecipes = new HashSet<>(CraftingManager.recipes.keySet());
        vanillaFurnaceRecipes = new HashMap<>(RecipesFurnace.getInstance().recipes);
    }

    public boolean isVanillaCraftingRecipe(MinecraftKey key) {
        if (key == null) return false;
        return vanillaCraftingRecipes.contains(key);
    }

    public boolean isVanillaFurnaceRecipe(net.minecraft.server.v1_12_R1.ItemStack ingredient, net.minecraft.server.v1_12_R1.ItemStack result) {
        if (ingredient == null) return false;
        return vanillaFurnaceRecipes.entrySet().stream().anyMatch(entry ->
            NMSFurnaceManager.furnaceEqualsVanilla(RecipesFurnace.getInstance(), entry.getKey(), ingredient) &&
            NMSFurnaceManager.furnaceEqualsVanilla(RecipesFurnace.getInstance(), entry.getValue(), result));
    }


    @Override
    public void onLoad() {
        instance = this;
        
        //DEBUG!
        Map<MinecraftKey, ? extends MojangCraftingRecipe> serverRecipes = MojangRecipeDeserializer.get(this.getLogger()).readFromServerJar();
        serverRecipes.entrySet()
                .forEach(entry -> {
                        System.out.println("key: " + entry.getKey());
                        System.out.println("value: " + entry.getValue());
                        System.out.println(System.lineSeparator());
                        System.out.println(System.lineSeparator());
                });
        getLogger().info("DEBUG TOTAL = " + serverRecipes.size()); //432 loaded from server jar
        
        getServer().resetRecipes();
        
        int vanillaTotal = StreamSupport.stream(CraftingManager.recipes.spliterator(), false).mapToInt(recipe -> 1).sum();
        getLogger().info("VANILLA TOTAL (incl hidden): " + vanillaTotal); //443 in the crafting manager
        
        craftingManager = new CRCraftingManager();
        furnaceManager = new CRFurnaceManager(NMSFurnaceManager.getInstance());


        //define RecipeItemStackInjected subclass
        InjectedIngredient.inject();

        //let's hope no other plugins have added crafting recipes here
        recordVanillaRecipes();
    }


    @Override
    public void onEnable() {
        //listeners
        getServer().getPluginManager().registerEvents(new GuiOpenListener(this), this);

        //commands
        getCommand("managerecipes").setExecutor(new ManageRecipesCommandExecutor(this));
        //TODO migration command
        
        
        //TODO disable vanilla recipes, check config.
        
        
        //serializable stuff

        //nbt arrays
        ConfigurationSerialization.registerClass(ConfigurationSerializableByteArray.class);
        ConfigurationSerialization.registerClass(ConfigurationSerializableIntArray.class);
        ConfigurationSerialization.registerClass(ConfigurationSerializableLongArray.class);

        //bukkit namespaced key
        ConfigurationSerialization.registerClass(SerializableKey.class);

        //bukkit ingredients
        ConfigurationSerialization.registerClass(SimilarIngredient.class);
        ConfigurationSerialization.registerClass(WildcardIngredient.class);
        ConfigurationSerialization.registerClass(SimpleChoiceIngredient.class);
        ConfigurationSerialization.registerClass(SimpleEmptyIngredient.class);

        //bukkit recipes
        ConfigurationSerialization.registerClass(SimpleShapedRecipe.class);
        ConfigurationSerialization.registerClass(SimpleShapelessRecipe.class);

        //nms mirrors
        //ConfigurationSerialization.registerClass(Bukkit2NMSIngredient.class);
        //ConfigurationSerialization.registerClass(Bukkit2NMSCraftingRecipe.class);
        //TODO bukkit ingredients don't have to be ConfigurationSerializable per se.
        //TODO find something for this (SerializableBukkit2NMSIngredient and SerializableBukkit2NMSCraftingRecipe classes?)

        //nms ingredient wrappers
        ConfigurationSerialization.registerClass(CRChoiceIngredient.class);
        ConfigurationSerialization.registerClass(CREmptyIngredient.class);

        //nms recipe wrappers
        //TODO remove all of this? I don't want to deal with the ConfigurationSerializable API actually.
//        ConfigurationSerialization.registerClass(CRShapedRecipe.class);
//        ConfigurationSerialization.registerClass(CRShapelessRecipe.class);
//        ConfigurationSerialization.registerClass(CRArmorDyeRecipe.class);
//        ConfigurationSerialization.registerClass(CRBannerAddPatternRecipe.class);
//        ConfigurationSerialization.registerClass(CRBannerDuplicateRecipe.class);
//        ConfigurationSerialization.registerClass(CRBookCloneRecipe.class);
//        ConfigurationSerialization.registerClass(CRFireworksRecipe.class);
//        ConfigurationSerialization.registerClass(CRMapCloneRecipe.class);
//        ConfigurationSerialization.registerClass(CRMapExtendRecipe.class);
//        ConfigurationSerialization.registerClass(CRRepairRecipe.class);
//        ConfigurationSerialization.registerClass(CRShieldDecorationRecipe.class);
//        ConfigurationSerialization.registerClass(CRShulkerBoxDyeRecipe.class);
//        ConfigurationSerialization.registerClass(CRTippedArrowRecipe.class);

        //furnace
        ConfigurationSerialization.registerClass(SimpleFixedFurnaceRecipe.class);
        ConfigurationSerialization.registerClass(CRFixedFurnaceRecipe.class);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static CustomRecipesPlugin getInstance() {
        return instance;
    }


    @Override
    public CRCraftingManager getCraftingManager() {
        return craftingManager;
    }

    @Override
    public CRFurnaceManager getFurnaceManager() {
        return furnaceManager;
    }


    @Override
    public ShapedRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapedRecipe bukkitRecipe) {
        String[] pattern = bukkitRecipe.getShape();
        Map<Character, SimpleChoiceIngredient> ingredientMap = bukkitRecipe.getIngredientMap().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> SimpleChoiceIngredient.fromChoices(e.getValue())));
        
        Shape shape = new Shape(pattern, ingredientMap);
        
        SimpleShapedRecipe simple = new SimpleShapedRecipe(bukkitRecipe.getKey(), bukkitRecipe.getResult(), shape);
        CraftingRecipe byKey = craftingManager.getRecipe(bukkitRecipe.getKey()); //can we do better? get by result and by ingredients?
        return simple.equals(byKey) ? (ShapedRecipe) byKey : simple;
    }

    @Override
    public ShapelessRecipe asCustomRecipesMirror(org.bukkit.inventory.ShapelessRecipe bukkitRecipe) {
        List<? extends ChoiceIngredient> ingredients = bukkitRecipe.getIngredientList().stream()
                .map(SimpleChoiceIngredient::fromChoices)
                .collect(Collectors.toList());

        SimpleShapelessRecipe simple = new SimpleShapelessRecipe(bukkitRecipe.getKey(), bukkitRecipe.getResult(), ingredients);
        CraftingRecipe byKey = craftingManager.getRecipe(bukkitRecipe.getKey()); //can we do better? get by result and by ingredients?
        return simple.equals(byKey) ? (ShapelessRecipe) byKey : simple;
    }

    @Override
    public FixedFurnaceRecipe asCustomRecipesMirror(org.bukkit.inventory.FurnaceRecipe bukkitRecipe) {
        FurnaceRecipe recipe = furnaceManager.getRecipe(bukkitRecipe.getInput());
        if (!(recipe instanceof FixedFurnaceRecipe)) {
            return new SimpleFixedFurnaceRecipe(NamespacedKey.randomKey(), bukkitRecipe.getInput(), bukkitRecipe.getResult(), bukkitRecipe.getExperience());
        }

        FixedFurnaceRecipe fixedRecipe = (FixedFurnaceRecipe) recipe;
        if (!Objects.equals(fixedRecipe.getResult(), bukkitRecipe.getResult()) ||
                !Objects.equals(Float.floatToIntBits(fixedRecipe.getExperience()), Float.floatToIntBits(bukkitRecipe.getExperience()))) {

            MinecraftKey key = CraftNamespacedKey.toMinecraft(getKey(InventoryUtils.getItemName(bukkitRecipe.getInput())));
            NMSFixedFurnaceRecipe nmsFurnaceRecipe  = new NMSFixedFurnaceRecipe(key,
                    CraftItemStack.asNMSCopy(bukkitRecipe.getInput()),
                    CraftItemStack.asNMSCopy(bukkitRecipe.getResult()),
                    bukkitRecipe.getExperience());
            fixedRecipe = new CRFixedFurnaceRecipe(nmsFurnaceRecipe);
        }
        return fixedRecipe;
    }


    public void save(String recipeType, String fileName, NBTSerializable recipe) {
        File saveFolder = saveFolder(recipeType);

        File saveFile = new File(saveFolder, fileName);
        writers.get(recipeType).accept(recipe, saveFile);
    }

    public Recipe load(String recipeType, File saveFile) {
        return readers.get(recipeType).apply(saveFile);
    }

    public File saveFolder(String recipeType) {
        File folder = new File(getDataFolder(), recipeType);
        if (!folder.exists()) folder.mkdirs();
        return folder;
    }

    public File disabledFolder(String recipeType) {
        File saveFolder = saveFolder(recipeType);
        File disabledFolder = new File(saveFolder, "disabled");
        disabledFolder.mkdirs();
        return disabledFolder;
    }

    public void saveCraftingRecipeFile(String recipeType, CRCraftingRecipe<?, ?> recipe) {
        String fileName = craftingRecipeFileName(recipe);
        if (isVanillaCraftingRecipe(recipe.getHandle().getKey())) {
            File disabledFolder = disabledFolder(recipeType);
            File disabledRecipeFile = new File(disabledFolder, fileName);
            disabledRecipeFile.delete();
        } else {
            if (recipe instanceof NBTSerializable) {
                save(recipeType, fileName, (NBTSerializable) recipe);
            } else {
                getLogger().severe("CRCraftingRecipe " + recipe + " is not NBTSerializable!");
            }
        }
    }

    public void saveFurnaceRecipeFile(CRFixedFurnaceRecipe recipe) {
        String fileName = furnaceRecipeFileName(recipe);
        boolean vanilla = false;

        NMSFixedFurnaceRecipe nmsRecipe = recipe.getHandle();
        net.minecraft.server.v1_12_R1.ItemStack nmsIngredient = nmsRecipe.getIngredient();

        vanilla = isVanillaFurnaceRecipe(nmsIngredient, nmsRecipe.getResult());

        if (vanilla) {
            File disabledFolder = disabledFolder("furnace");
            File disabledRecipeFile = new File(disabledFolder, fileName);
            disabledRecipeFile.delete();
        } else {
            save("furnace", fileName, recipe);
        }
    }


    /**
     * Delete recipe or disable vanilla recipe
     *
     * @param recipeType shaped or shapeless
     * @param recipe the vanilla recipe
     */
    //TODO replace/re-implement this method for the new file structure
    @Deprecated
    public void disableCraftingRecipeFile(String recipeType, CRCraftingRecipe recipe) {
        String fileName = craftingRecipeFileName(recipe);
        if (isVanillaCraftingRecipe(recipe.getHandle().getKey())) {

            //disable
            File disabledFolder = disabledFolder(recipeType);
            File disabledFile = new File(disabledFolder, fileName);
            try {
                if (recipe instanceof NBTSerializable) {
                    NBTSerializable nbtSerializableRecipe = (NBTSerializable) recipe;
                    NBTUtil.writeNBTTagCompound(disabledFile, nbtSerializableRecipe.serializeToNbt());
                } else {
                    getLogger().severe("Recipe " + recipe + " is not serializable to NBT!");
                }
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Could not disable vanilla crafting recipe!", e);
            }
        } else {

            //delete
            File saveFolder = saveFolder(recipeType);
            File saveFile = new File(saveFolder, fileName);
            saveFile.delete();
        }
    }

    /**
     * Delete recipe or disable vanilla recipe
     *
     * @param recipe the furnace recipe
     */
    public void disableFurnaceRecipeFile(CRFixedFurnaceRecipe recipe) {
        String fileName = furnaceRecipeFileName(recipe);

        boolean vanilla = false;
        NMSFixedFurnaceRecipe nmsRecipe = recipe.getHandle();
        net.minecraft.server.v1_12_R1.ItemStack nmsIngredient = nmsRecipe.getIngredient();

        vanilla = isVanillaFurnaceRecipe(nmsIngredient, nmsRecipe.getResult());

        if (vanilla) {
            //disable
            File disabledFolder = disabledFolder("furnace");
            File disabledFile = new File(disabledFolder, fileName);
            try {
                NBTUtil.writeNBTTagCompound(disabledFile, recipe.serializeToNbt());
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Could not disable vanilla crafting recipe!", e);
            }
        } else {
            //delete
            File saveFolder = saveFolder("furnace");
            File saveFile = new File(saveFolder, fileName);
            saveFile.delete();
        }
    }

    public static String craftingRecipeFileName(CRCraftingRecipe<?, ?> recipe) {
        NamespacedKey key = recipe.getKey();
        return key.toString().replace(':', '_') + ".dat";
    }

    public static String furnaceRecipeFileName(CRFurnaceRecipe recipe) {
        NamespacedKey key = recipe.getKey();
        return key.toString().replace(':', '_') + ".dat";
    }


    public void setCraftingManager(CRCraftingManager craftingManager) {
        this.craftingManager = Objects.requireNonNull(craftingManager);
    }

    public void setFurnaceManager(CRFurnaceManager furnaceManager) {
        this.furnaceManager = Objects.requireNonNull(furnaceManager);
    }

    @SuppressWarnings("deprecation")
    public NamespacedKey getKey(String string) {
        String[] split = string.split(":");
        return split.length == 1 ? new NamespacedKey(this, string) : new NamespacedKey(split[0], split[1]);
    }

}
