package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.util.function.Supplier;

public abstract class EntirelyRefreshableCacheServiceImplBase<ID, T extends CacheableData<ID>>
        extends CacheServiceImplBase<ID, T>
        implements RefreshableCacheService<ID, T> {

    private final int dataPageSize;
    private final Supplier<MyCache<ID, T>> cacheSupplier;

    public EntirelyRefreshableCacheServiceImplBase(Supplier<MyCache<ID, T>> cacheSupplier, int dataPageSize) {
        super(cacheSupplier.get());
        this.dataPageSize = dataPageSize;
        this.cacheSupplier = cacheSupplier;
    }

    @Override
    public void refresh() {
        MyCache<ID, T> newCache = prepareNewCache();
        setDataCache(newCache);
    }

    private MyCache<ID, T> prepareNewCache() {
        MyCache<ID, T> newCache = cacheSupplier.get();

        int pageNumber = 0;

        while (populateCacheWithDataPage(newCache, pageNumber, dataPageSize)) {
            pageNumber++;
        }

        return newCache;
    }

    private boolean populateCacheWithDataPage(MyCache<ID, T> cache, int page, int size) {
        CacheableDataPage<T> cacheableDataPage = this.loadDataPage(page, size);
        cacheableDataPage.data().forEach(data -> cache.put(data.getId(), data));
        return cacheableDataPage.pageNumber() < cacheableDataPage.pagesCount() - 1;
    }

    protected abstract CacheableDataPage<T> loadDataPage(int page, int size);
}
