package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

public interface ResettableCacheService<ID, T extends CacheableData<ID>> extends CacheService<ID, T> {

    void reset();
}
