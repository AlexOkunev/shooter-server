package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.util.Optional;

public abstract class CacheableDataCacheServiceImplBase<ID, T extends CacheableData<ID>> implements CacheableDataCacheService<ID, T> {
    private final MyCache<ID, T> dataCache;

    private final int dataPageSize;

    public CacheableDataCacheServiceImplBase(MyCache<ID, T> dataCache, int dataPageSize) {
        this.dataCache = dataCache;
        this.dataPageSize = dataPageSize;
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

        Optional<T> loadedData = loadReferenceDataById(id);
        loadedData.ifPresentOrElse(d -> dataCache.put(d.getId(), d), () -> dataCache.remove(id));

        return loadedData;
    }

    @Override
    public boolean loadDataPage(int page, int size) {
        CacheableDataPage<T> cacheableDataPage = loadReferenceDataPage(page, size);
        cacheableDataPage.data().forEach(data -> dataCache.put(data.getId(), data));
        return cacheableDataPage.pageNumber() < cacheableDataPage.pagesCount() - 1;
    }

    @Override
    public void clear() {
        dataCache.clear();
    }

    @Override
    public void loadAllData() {
        int pageNumber = 0;

        while (loadDataPage(pageNumber, dataPageSize)) {
            pageNumber++;
        }
    }

    @Override
    public void reloadAllData() {
        clear();
        loadAllData(); //TODO!!! исправить, небезопасно в плане потоков. или не чистить, или целиком загружать и менять структуру в поле dataCache
    }

    protected abstract CacheableDataPage<T> loadReferenceDataPage(int page, int size);

    protected abstract Optional<T> loadReferenceDataById(ID id);
}
