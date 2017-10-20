package com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.extended;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FilenameUtils;

import com.gmail.jannyboy11.customrecipes.impl.crafting.vanilla.extended.MojangShapedRecipe.Shape;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.server.v1_12_R1.CraftingManager;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NonNullList;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class MojangRecipeDeserializer implements JsonDeserializer<MojangCraftingRecipe> {
    
    private static MojangRecipeDeserializer instance;
    
    private final Logger logger;
    
    private MojangRecipeDeserializer(Logger logger) {
        this.logger = logger;
    }
    
    public static MojangRecipeDeserializer get(Logger logger) {
        return instance == null ? instance = new MojangRecipeDeserializer(logger) : instance;
    }
    

    @Override
    public MojangCraftingRecipe deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        if (type != MojangCraftingRecipe.class) {
            return null;
        }
        
        JsonObject obj = json.getAsJsonObject();
        
        try {
            switch (obj.get("type").getAsString()) {
                case "crafting_shaped": return deserializeShaped(obj);
                case "crafting_shapeless": return deserializeShapeless(obj);
            }
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Couldn't deserialize vanilla crafting recipe: " + json.toString(), e);
        }
        
        return null;
    }
    
    private MojangShapedRecipe deserializeShaped(JsonObject object) {
        ItemStack result = deserializeItemStack(object.get("result").getAsJsonObject());
        String group = object.has("group") ? object.get("group").getAsString() : "";
        
        String[] pattern = StreamSupport.stream(object.get("pattern").getAsJsonArray().spliterator(), false)
                .map(JsonElement::getAsString)
                .toArray(String[]::new);
        
        Map<Character, RecipeItemStack> keys = object.get("key").getAsJsonObject().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().charAt(0), e -> deserializeRecipeItemStack(e.getValue())));
        
        Shape shape = new Shape(pattern, keys);
        
        return new MojangShapedRecipe(group, result, shape);
    }
    
    private MojangShapelessRecipe deserializeShapeless(JsonObject object) {
        ItemStack result = deserializeItemStack(object.get("result").getAsJsonObject());
        String group = object.has("group") ? object.get("group").getAsString() : "";
        NonNullList<RecipeItemStack> ingredients = deserializeIngredients(object.get("ingredients"));                

        return new MojangShapelessRecipe(group, result, ingredients);
    }

    
    private NonNullList<RecipeItemStack> deserializeIngredients(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            return deserializeIngredients(jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonObject()) {
            return NonNullList.a(RecipeItemStack.a,
                    RecipeItemStack.a(new ItemStack[] {deserializeItemStack(jsonElement.getAsJsonObject())}));
        }
        
        return NonNullList.a();
    }
    
    private NonNullList<RecipeItemStack> deserializeIngredients(JsonArray jsonArray) {
        return StreamSupport.stream(jsonArray.spliterator(), false)
                .map(this::deserializeRecipeItemStack)
                .collect(Collectors.toCollection(NonNullList::a));
    }
    
    private RecipeItemStack deserializeRecipeItemStack(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return RecipeItemStack.a(new ItemStack[] {deserializeItemStack(jsonElement.getAsJsonObject())});
        } else if (jsonElement.isJsonArray()) {
            return RecipeItemStack.a(StreamSupport.stream(jsonElement.getAsJsonArray().spliterator(), false)
                    .map(JsonElement::getAsJsonObject)
                    .map(this::deserializeItemStack)
                    .toArray(ItemStack[]::new));
        }
        
        logger.severe("Tried to deserialize ingredient: " + jsonElement);
        
        return RecipeItemStack.a;
    }

    private ItemStack deserializeItemStack(JsonObject object) {
        MinecraftKey itemKey = new MinecraftKey(object.get("item").getAsString());
        Item item = Item.REGISTRY.get(itemKey);
        ItemStack stack = new ItemStack(item);
        if (object.has("count")) stack.setCount(object.get("count").getAsInt());
        if (object.has("data")) stack.setData(object.get("data").getAsInt());
        return stack;
    }
    
    public Map<MinecraftKey, ? extends MojangCraftingRecipe> readFromServerJar() {
        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeHierarchyAdapter(MojangCraftingRecipe.class, this)
                .create();
        
        URL url = CraftingManager.class.getResource("/assets/.mcassetsroot");
        if (url == null) {
            logger.severe("Couldn't find .mcassetsroot");
            return Map.of();
        }
        
        FileSystem fileSystem = null;
        
        try {
            URI uri = url.toURI();
            final Path recipesRootPath;
            if ("file".equals(uri.getScheme())) {
                recipesRootPath = Paths.get(CraftingManager.class.getResource("/assets/minecraft/recipes").toURI());
            } else if ("jar".equals(uri.getScheme())) {
                fileSystem = FileSystems.newFileSystem(uri, Map.of());
                recipesRootPath = fileSystem.getPath("/assets/minecraft/recipes");
            } else {
                logger.severe("Unsupported URI scheme: " + uri.getScheme() + " in uri: " + uri);
                return Map.of();
            }
            
            return Files.walk(recipesRootPath)
                    .filter(p -> "json".equals(FilenameUtils.getExtension(p.toString())))
                    .map(recipeFilePath -> {
                
                        Path relativePath = recipesRootPath.relativize(recipeFilePath);
                        String recipeFileName = FilenameUtils.removeExtension(relativePath.toString()).replaceAll("\\\\", "/");
                        MinecraftKey recipeKey = new MinecraftKey(recipeFileName);
                        
                        try (BufferedReader reader = Files.newBufferedReader(recipeFilePath)){
                            return Map.entry(recipeKey, gson.fromJson(reader, MojangCraftingRecipe.class));
                        } catch (IOException e) {
                            //could not read
                            logger.severe("Could not read json recipe: " + recipeFilePath);
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            
        } catch (URISyntaxException e) {
            //not an uri format - should not occur
            logger.severe("Not an URI format!");
            e.printStackTrace();
        } catch (IOException ioException) {
            logger.severe("FileSystem not avaiable!");
            ioException.printStackTrace();
        } finally {
            if (fileSystem != null) {
                try {
                    fileSystem.close();
                } catch (IOException e) {
                    logger.severe("Could not close FileSystem!");
                    e.printStackTrace();
                }
            }
        }
        
        return Map.of();
    }
}
