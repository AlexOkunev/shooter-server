package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<T> getByIds(Collection<ID> ids) {
        Map<ID, T> map = getByIdsAsMap(ids);
        return ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Map<ID, T> getByIdsAsMap(Collection<ID> ids) {
        Map<ID, T> tempMap = ids.stream()
                .map(id -> dataCache.get(id))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        T::getId,
                        Function.identity(),
                        (existing, replacement) -> existing,
                        HashMap::new
                ));

        List<ID> notFoundIds = ids.stream()
                .filter(id -> !tempMap.containsKey(id))
                .distinct()
                .toList();

        List<T> loadedDataList = produceDataByIds(notFoundIds);

        loadedDataList.forEach(data -> tempMap.put(data.getId(), data));

        return Collections.unmodifiableMap(tempMap);
    }

    @Override
    public void clear() {
        dataCache.clear();
    }

    protected abstract Optional<T> produceDataById(ID id);

    protected abstract List<T> produceDataByIds(Collection<ID> id);
}
