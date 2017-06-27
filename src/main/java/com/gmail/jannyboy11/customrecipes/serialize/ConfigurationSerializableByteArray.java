package com.gmail.jannyboy11.customrecipes.serialize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class ConfigurationSerializableByteArray implements ConfigurationSerializable {
	
	private final byte[] bytes;

	public ConfigurationSerializableByteArray(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public ConfigurationSerializableByteArray(Map<String, Object> map) {
		List<Number> byteList = (List) map.get("bytes");
		bytes = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++) {
			bytes[i] = byteList.get(i).byteValue();
		}
	}

	@Override
	public Map<String, Object> serialize() {
		return Collections.singletonMap("bytes", byteList());
	}

	private List<Byte> byteList() {
		List<Byte> list = new ArrayList<>(bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			list.add(bytes[i]);
		}
		return list;
	}
	
	public byte[] getBytes() {
		return bytes;
	}

}
