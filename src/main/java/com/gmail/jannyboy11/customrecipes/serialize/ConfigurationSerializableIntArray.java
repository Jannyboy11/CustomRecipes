package com.gmail.jannyboy11.customrecipes.serialize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ConfigurationSerializableIntArray implements ConfigurationSerializable {
	
	private final int[] ints;
	
	public ConfigurationSerializableIntArray(int[] ints) {
		this.ints = ints;
	}
	
	public ConfigurationSerializableIntArray(Map<String, Object> map) {
		List<Number> intList = (List) map.get("ints");
		ints = new int[intList.size()];
		for (int i = 0; i < intList.size(); i++) {
			ints[i] = intList.get(i).intValue();
		}
	}

	@Override
	public Map<String, Object> serialize() {
		return Collections.singletonMap("ints", intList());
	}

	private List<Integer> intList() {
		List<Integer> list = new ArrayList<>(ints.length);
		for (int i = 0; i < ints.length; i++) {
			list.add(ints[i]);
		}
		return list;
	}

	public int[] getInts() {
		return ints;
	}
}
