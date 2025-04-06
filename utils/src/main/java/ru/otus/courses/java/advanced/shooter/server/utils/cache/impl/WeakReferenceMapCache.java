package ru.otus.courses.java.advanced.shooter.server.utils.cache.impl;


import ru.otus.courses.java.advanced.shooter.server.utils.cache.MyCache;

import java.lang.ref.WeakReference;
import java.util.Map;

public class WeakReferenceMapCache<K, V> extends ReferenceMapCacheImplBase<K, V, WeakReference<V>> implements MyCache<K, V> {

    public WeakReferenceMapCache(Map<K, WeakReference<V>> map) {
        super(map);
    }

    @Override
    protected V extractValue(WeakReference<V> wrapper) {
        return wrapper != null ? wrapper.get() : null;
    }

    @Override
    protected WeakReference<V> wrapValue(V value) {
        return value != null ? new WeakReference<>(value) : null;
    }
}