package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl;


import lombok.RequiredArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public abstract class MapCacheImplBase<K, V, W> implements MyCache<K, V> {
    private final Map<K, W> map;

    @Override
    public V get(K key) {
        W valueWrapper = map.get(key);
        return extractValue(valueWrapper);
    }

    @Override
    public boolean containsKey(K key) {
        W valueWrapper = map.get(key);
        return valueWrapper != null && isNotEmptyValue(valueWrapper);
    }

    @Override
    public V put(K key, V value) {
        W valueWrapper = wrapValue(value);
        map.put(key, valueWrapper);
        return get(key);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public long count() {
        Collection<W> wrappers = map.values();
        return wrappers.stream()
                .filter(this::isNotEmptyValue)
                .count();
    }

    @Override
    public Collection<K> keys() {
        Set<Map.Entry<K, W>> entries = map.entrySet();
        return entries.stream()
                .filter(entry -> isNotEmptyValue(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public void clear() {
        map.clear();
    }

    protected abstract V extractValue(W wrapper);

    protected abstract W wrapValue(V value);

    protected boolean isNotEmptyValue(W wrapper) {
        return wrapper != null;
    }
}
