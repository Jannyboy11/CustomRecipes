package com.gmail.jannyboy11.customrecipes.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class SerializableKey implements ConfigurationSerializable, Keyed {
    
    protected final NamespacedKey key;
    
    public SerializableKey(String namespace, String key) {
        this(new NamespacedKey(namespace, key));
    }
    
    public SerializableKey(NamespacedKey namespacedKey) {
        this.key = Objects.requireNonNull(namespacedKey);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("namespace", key.getNamespace());
        map.put("key", key.getKey());
        return map;
    }
    
    public static SerializableKey deserialize(Map<String, Object> map) {
        String namespace = String.valueOf(map.get("namespace"));
        String key = String.valueOf(map.get("key"));
        return new SerializableKey(namespace, key);
    }
    
    @Override
    public NamespacedKey getKey() {
        return key;
    }

}
