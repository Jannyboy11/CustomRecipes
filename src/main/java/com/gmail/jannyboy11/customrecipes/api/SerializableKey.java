package com.gmail.jannyboy11.customrecipes.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Represents a {@link NamespacedKey} that implements {@link ConfigurationSerializable}.
 * 
 * @author Jan
 */
public class SerializableKey implements ConfigurationSerializable, Keyed {
    
    private final NamespacedKey key;

    /**
     * Instantiates this key using a namespace and a key.
     *
     * @param namespace the namespace
     * @param key the key
     */
    @SuppressWarnings("deprecation")
    public SerializableKey(String namespace, String key) {
        this(new NamespacedKey(namespace, key));
    }

    /**
     * Instantiates this key using a NamepacedKey.
     *
     * @param namespacedKey the key that needs to be serialized
     */
    public SerializableKey(NamespacedKey namespacedKey) {
        this.key = Objects.requireNonNull(namespacedKey, "namespacedKey cannot be null");
    }

    /**
     * Serializes this key.
     *
     * @return a map containing the namespace and the key
     */
    @Override
    public Map<String, Object> serialize() {
        return Map.of("namespace", key.getNamespace(),
                "key", key.getKey());
    }

    /**
     * Deserialization method.
     *
     * @param map the map containing the serialized fields
     * @return a new SerializableKey
     */
    public static SerializableKey deserialize(Map<String, Object> map) {
        String namespace = String.valueOf(map.get("namespace"));
        String key = String.valueOf(map.get("key"));
        return new SerializableKey(namespace, key);
    }

    /**
     * Get the key.
     *
     * @return the key
     */
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
