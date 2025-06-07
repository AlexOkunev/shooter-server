package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.impl;



import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.util.HashMap;

public class SimpleMapCache<K, V> extends MapCacheImplBase<K, V, V> implements MyCache<K, V> {

    public SimpleMapCache() {
        super(new HashMap<>());
    }

    @Override
    protected V extractValue(V wrapper) {
        return wrapper;
    }

    @Override
    protected V wrapValue(V value) {
        return value;
    }
}
