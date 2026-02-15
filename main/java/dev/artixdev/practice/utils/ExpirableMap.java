package dev.artixdev.practice.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import dev.artixdev.practice.interfaces.Expirable;

public class ExpirableMap<K, V> implements Map<K, V>, Expirable<K> {
    
    private final HashMap<K, Long> timestamps = new HashMap<>();
    private final HashMap<K, V> elements;
    private final long expirationTime;

    public ExpirableMap(TimeUnit timeUnit, long duration) {
        this.elements = new HashMap<>();
        this.expirationTime = timeUnit.toNanos(duration);
    }

    @Override
    public void onExpire(K key) {
        // Handle key expiration
    }

    @Override
    public long getTimestamp(K key) {
        return timestamps.getOrDefault(key, 0L);
    }

    private void clearExpired() {
        long currentTime = System.nanoTime();
        Iterator<Entry<K, Long>> iterator = timestamps.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Entry<K, Long> entry = iterator.next();
            if ((currentTime - entry.getValue()) > expirationTime) {
                onExpire(entry.getKey());
                elements.remove(entry.getKey());
                iterator.remove();
            }
        }
    }

    @Override
    public int size() {
        clearExpired();
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        clearExpired();
        return elements.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        clearExpired();
        return elements.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        clearExpired();
        return elements.containsValue(value);
    }

    @Override
    public V get(Object key) {
        clearExpired();
        return elements.get(key);
    }

    @Override
    public V put(K key, V value) {
        clearExpired();
        timestamps.put(key, System.nanoTime());
        return elements.put(key, value);
    }

    @Override
    public V remove(Object key) {
        clearExpired();
        timestamps.remove(key);
        return elements.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        clearExpired();
        long currentTime = System.nanoTime();
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            timestamps.put(entry.getKey(), currentTime);
        }
        elements.putAll(m);
    }

    @Override
    public void clear() {
        elements.clear();
        timestamps.clear();
    }

    @Override
    public Set<K> keySet() {
        clearExpired();
        return Collections.unmodifiableSet(elements.keySet());
    }

    @Override
    public Collection<V> values() {
        clearExpired();
        return Collections.unmodifiableCollection(elements.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        clearExpired();
        return Collections.unmodifiableSet(elements.entrySet());
    }
}
