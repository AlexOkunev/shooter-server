package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.util.Optional;

public abstract class CacheServiceImplBase<ID, T extends CacheableData<ID>> implements CacheService<ID, T> {

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private volatile MyCache<ID, T> dataCache;

    public CacheServiceImplBase(MyCache<ID, T> dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public void put(T data) {
        dataCache.put(data.getId(), data);
    }

    @Override
    public void deleteById(ID id) {
        dataCache.remove(id);
    }

    @Override
    public long count() {
        return dataCache.count();
    }

    @Override
    public Optional<T> getById(ID id) {
        Optional<T> dataFromCache = Optional.ofNullable(dataCache.get(id));
        if (dataFromCache.isPresent()) {
            return dataFromCache;
        }

        Optional<T> loadedData = produceDataById(id);
        loadedData.ifPresentOrElse(d -> dataCache.put(d.getId(), d), () -> dataCache.remove(id));

        return loadedData;
    }

    @Override
    public void clear() {
        dataCache.clear();
    }

    protected abstract Optional<T> produceDataById(ID id);
}
