package com.gmail.jannyboy11.customrecipes.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a {@link org.bukkit.NamespacedKey} that implements ConfigurationSerializable.
 * 
 * @author Jan
 */
public class SerializableKey implements ConfigurationSerializable, Keyed {
    
    protected final NamespacedKey key;
    
    public SerializableKey(String namespace, String key) {
        this(new NamespacedKey(namespace, key));
    }
    
    public SerializableKey(NamespacedKey namespacedKey) {
        this.key = Objects.requireNonNull(namespacedKey, "namespacedKey cannot be null");
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
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SerializableKey)) return false;
        
        SerializableKey that = (SerializableKey) o;
        return Objects.equals(this.getKey(), that.getKey());
    }
    
    @Override
    public int hashCode() {
        return getKey().hashCode();
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "{key()=" + getKey() + "}";
    }

}
