package com.gmail.jannyboy11.customrecipes.serialize;

import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.gmail.jannyboy11.customrecipes.util.NBTUtil;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

public interface NBTSerializable extends ConfigurationSerializable {
	
	public NBTTagCompound serializeToNbt();
	
	public default Map<String, Object> serialize() {
		return NBTUtil.toMap(serializeToNbt());
	}

}
