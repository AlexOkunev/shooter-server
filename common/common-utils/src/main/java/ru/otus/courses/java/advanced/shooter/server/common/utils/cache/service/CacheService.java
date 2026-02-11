package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;


import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

import java.util.Optional;

public interface CacheService<ID, T extends CacheableData<ID>> {

    void put(T data);

    void deleteById(ID id);

    long count();

    Optional<T> getById(ID id);

    void clear();
}
