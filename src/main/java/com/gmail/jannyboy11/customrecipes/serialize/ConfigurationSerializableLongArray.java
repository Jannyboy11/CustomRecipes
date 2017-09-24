package com.gmail.jannyboy11.customrecipes.serialize;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ConfigurationSerializableLongArray implements ConfigurationSerializable {
	
	private final long[] longs;
	
	public ConfigurationSerializableLongArray(long[] longs) {
		this.longs = longs;
	}
	
	public ConfigurationSerializableLongArray(Map<String, Object> map) {
		List<Number> longList = (List) map.get("longs");
		longs = new long[longList.size()];
		for (int i = 0; i < longList.size(); i++) {
			longs[i] = longList.get(i).longValue();
		}
	}

	@Override
	public Map<String, Object> serialize() {
		return Collections.singletonMap("longs", longList());
	}

	private List<Long> longList() {
		List<Long> list = new ArrayList<>(longs.length);
		for (int i = 0; i < longs.length; i++) {
			list.add(longs[i]);
		}
		return list;
	}

	public long[] getLongs() {
		return longs;
	}
	
}
