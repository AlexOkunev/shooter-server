package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.lang.ref.SoftReference;
import java.util.Map;

public class SoftReferenceMapCache<K, V> extends ReferenceMapCacheImplBase<K, V, SoftReference<V>> implements MyCache<K, V> {

    public SoftReferenceMapCache(Map<K, SoftReference<V>> map) {
        super(map);
    }

    @Override
    protected V extractValue(SoftReference<V> wrapper) {
        return wrapper != null ? wrapper.get() : null;
    }

    @Override
    protected SoftReference<V> wrapValue(V value) {
        return value != null ? new SoftReference<>(value) : null;
    }
}
