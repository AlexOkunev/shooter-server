package ru.otus.courses.java.advanced.shooter.server.utils.cache.impl;

import ru.otus.courses.java.advanced.shooter.server.utils.cache.MyCache;

import java.lang.ref.Reference;
import java.util.Map;

public abstract class ReferenceMapCacheImplBase<Key, Value, RefWrapper extends Reference<Value>> extends MapCacheImplBase<Key, Value, RefWrapper> implements MyCache<Key, Value> {
    public ReferenceMapCacheImplBase(Map<Key, RefWrapper> map) {
        super(map);
    }

    @Override
    protected boolean isNotEmptyValue(RefWrapper wrapper) {
        return wrapper != null && wrapper.get() != null;
    }
}
