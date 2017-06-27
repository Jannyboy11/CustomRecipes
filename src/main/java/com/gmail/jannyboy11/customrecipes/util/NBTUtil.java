package com.gmail.jannyboy11.customrecipes.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmail.jannyboy11.customrecipes.serialize.ConfigurationSerializableByteArray;
import com.gmail.jannyboy11.customrecipes.serialize.ConfigurationSerializableIntArray;
import com.gmail.jannyboy11.customrecipes.serialize.ConfigurationSerializableLongArray;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagByteArray;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagDouble;
import net.minecraft.server.v1_12_R1.NBTTagEnd;
import net.minecraft.server.v1_12_R1.NBTTagFloat;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagIntArray;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagLong;
import net.minecraft.server.v1_12_R1.NBTTagLongArray;
import net.minecraft.server.v1_12_R1.NBTTagShort;
import net.minecraft.server.v1_12_R1.NBTTagString;
import net.minecraft.server.v1_12_R1.RecipeItemStack;

public class NBTUtil {
	
	protected NBTUtil() {}
	
	
	// ============== IO ==============
	
	public static void writeNBTTagCompound(File saveFile, NBTTagCompound compound) throws IOException {
		File tempFile = new File(saveFile.getAbsolutePath() + ".tmp");
		if (tempFile.exists()) tempFile.delete();
		tempFile.createNewFile();
		
		NBTCompressedStreamTools.a(compound, new FileOutputStream(tempFile));
		tempFile.renameTo(saveFile);
	}
	
	public static NBTTagCompound readNBTTagCompound(File saveFile) throws IOException {
		NBTTagCompound compound = NBTCompressedStreamTools.a(new FileInputStream(saveFile));
		return compound;
	}
	
	
	// ============== MinecraftKey ==============
	
	public static NBTTagCompound serializeKey(MinecraftKey key) {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("namespace", key.b());
		compound.setString("key", key.getKey());
		return compound;
	}
	
	public static MinecraftKey deserializeKey(NBTTagCompound compound) {
		return new MinecraftKey(compound.getString("namespace"), compound.getString("key"));
	}
	
	
	// ============== RecipeItemStack ==============
	
	public static NBTTagCompound serializeRecipeItemStack(RecipeItemStack recipeItemStack) {
		if (recipeItemStack == RecipeItemStack.a) return new NBTTagCompound(); //empty ingredient -> empty compound
		
		NBTTagCompound compound = new NBTTagCompound();
		if (recipeItemStack.choices.length == 0) return compound; //not the empty ingredient, but no choices
		
		NBTTagList nbtChoices = new NBTTagList();
		for (ItemStack choice : recipeItemStack.choices) {
			NBTTagCompound choiceCompound = new NBTTagCompound();
			choice.save(choiceCompound);
			nbtChoices.add(choiceCompound);
		}
		compound.set("choices", nbtChoices);
		
		return compound;
	}
	
	public static RecipeItemStack deserializeRecipeItemStack(NBTTagCompound compound) {
		if (compound.isEmpty()) return RecipeItemStack.a; //empty compound -> empty ingredient
		
		if (compound.hasKeyOfType("choices", 9 /*list = 9*/)) {
			NBTTagList choicesList = compound.getList("choices", 10 /*subtype = compound = 10*/);
			List<ItemStack> itemsList = new ArrayList<>();
			for (int i = 0; i < choicesList.size(); i++) {
				NBTTagCompound choiceCompound = choicesList.get(i);
				ItemStack stack = new ItemStack(choiceCompound);
				itemsList.add(stack);
			}
			
			ItemStack[] choices = itemsList.toArray(new ItemStack[itemsList.size()]);
			return RecipeItemStack.a(choices);
		}
		
		return RecipeItemStack.a(new ItemStack[0]); //not an empty compound, but no choices.
	}
	
	
	
	// ============== ConfigurationSerializable Conversion ==============
	
	public static Map<String, Object> toMap(NBTTagCompound compound) {
		Map<String, Object> map = new HashMap<>();
		for (String key : compound.c()) { //c() => keyset
			NBTBase value = compound.get(key);
			map.put(key, toObject(value));
		}
		return map;
	}
	
	public static NBTTagCompound fromMap(Map<String, ?> map) {
		NBTTagCompound compound = new NBTTagCompound();
		map.forEach((key, value) -> compound.set(key, fromObject(value)));
		return compound;
	}
	
	public static NBTTagList fromList(List<?> list) {
		NBTTagList nbtList = new NBTTagList();
		list.forEach(o -> nbtList.add(fromObject(o)));
		return nbtList;
	}
	
	public static List toList(NBTTagList nbtList) {
		List list = new ArrayList();
		for (int i = 0; i < nbtList.size(); i++) {
			list.add(toObject(nbtList.i(i)));
		}
		return list;
	}
	
	public static Object toObject(NBTBase nbtBase) {
		if (nbtBase instanceof NBTTagEnd) {
			return null;
		} else if (nbtBase instanceof NBTTagByte) {
			return ((NBTTagByte) nbtBase).g();
		} else if (nbtBase instanceof NBTTagShort) {
			return ((NBTTagShort) nbtBase).f();
		} else if (nbtBase instanceof NBTTagInt) {
			return ((NBTTagInt) nbtBase).e();
		} else if (nbtBase instanceof NBTTagLong) {
			return ((NBTTagLong) nbtBase).d();
		} else if (nbtBase instanceof NBTTagFloat) {
			return ((NBTTagFloat) nbtBase).i();
		} else if (nbtBase instanceof NBTTagDouble) {
			return ((NBTTagDouble) nbtBase).asDouble();
		} else if (nbtBase instanceof NBTTagByteArray) {
			byte[] bytes = ((NBTTagByteArray) nbtBase).c();
			return new ConfigurationSerializableByteArray(bytes);
		} else if (nbtBase instanceof NBTTagIntArray) {
			int[] ints = ((NBTTagIntArray) nbtBase).d();
			return new ConfigurationSerializableIntArray(ints);
		} else if (nbtBase instanceof NBTTagLongArray) {
			NBTTagLongArray array = (NBTTagLongArray) nbtBase;
			long[] longs = (long[]) ReflectionUtil.getDeclaredFieldValue(array, "b");
			return new ConfigurationSerializableLongArray(longs);
		} else if (nbtBase instanceof NBTTagString) {
			return ((NBTTagString) nbtBase).c_();
		} else if (nbtBase instanceof NBTTagList) {
			return toList((NBTTagList) nbtBase);
		} else if (nbtBase instanceof NBTTagCompound) {
			return toMap((NBTTagCompound) nbtBase);
		}
		
		throw new RuntimeException("Unrecognized NBT type: " + nbtBase);
	}
	
	public static NBTBase fromObject(Object o) {

		if (o instanceof NBTTagEnd) {
			return null;
		} else if (o instanceof Byte) {
			return new NBTTagByte((Byte) o);
		} else if (o instanceof Short) {
			return new NBTTagShort((Short) o);
		} else if (o instanceof Integer) {
			return new NBTTagInt((Integer) o);
		} else if (o instanceof Long) {
			return new NBTTagLong((Long) o);
		} else if (o instanceof Float) {
			return new NBTTagFloat((Float) o);
		} else if (o instanceof Double) {
			return new NBTTagDouble((Double) o);
		} else if (o instanceof byte[]) {
			return new NBTTagByteArray((byte[]) o);
		} else if (o instanceof ConfigurationSerializableByteArray) {
			return new NBTTagByteArray(((ConfigurationSerializableByteArray) o).getBytes());
		} else if (o instanceof int[]) {
			return new NBTTagIntArray((int[]) o);
		} else if (o instanceof ConfigurationSerializableIntArray) {
			return new NBTTagIntArray(((ConfigurationSerializableIntArray) o).getInts());
		} else if (o instanceof long[]) {
			return new NBTTagLongArray((long[]) o);
		} else if (o instanceof ConfigurationSerializableLongArray) {
			return new NBTTagLongArray(((ConfigurationSerializableLongArray) o).getLongs());
		} else if (o instanceof String) {
			return new NBTTagString((String) o);
		} else if (o instanceof List) {
			return fromList((List) o);
		} else if (o instanceof Map) {
			return fromMap((Map) o);
		}

		throw new RuntimeException("Object not nbt deserializable: " + o);
	}

	
	//dirty hack to get an NBTTagEnd
	protected static class NBTDummy extends NBTTagCompound {
		
		public static final NBTDummy INSTANCE = new NBTDummy();
		
		public final NBTTagEnd end() {
			return (NBTTagEnd) createTag((byte) 0);
		}
		
	}
	
	
	
	
	

}
