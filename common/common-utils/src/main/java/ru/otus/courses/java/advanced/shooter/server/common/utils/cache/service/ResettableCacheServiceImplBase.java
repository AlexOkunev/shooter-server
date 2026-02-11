package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

public abstract class ResettableCacheServiceImplBase<ID, T extends CacheableData<ID>>
        extends CacheServiceImplBase<ID, T>
        implements ResettableCacheService<ID, T> {

    public ResettableCacheServiceImplBase(MyCache<ID, T> dataCache) {
        super(dataCache);
    }

    @Override
    public void reset() {
        clear();
    }
}
