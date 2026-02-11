package ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableDataPage;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.structure.MyCache;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public abstract class IncrementallyRefreshableCacheServiceImplBase<ID, T extends CacheableData<ID>>
        extends CacheServiceImplBase<ID, T>
        implements RefreshableCacheService<ID, T> {

    private final int dataPageSize;
    private ZonedDateTime lastRefreshTime;

    public IncrementallyRefreshableCacheServiceImplBase(MyCache<ID, T> dataCache, int dataPageSize) {
        super(dataCache);
        this.dataPageSize = dataPageSize;
        this.lastRefreshTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0), ZoneOffset.UTC);
    }

    @Override
    public void refresh() {
        updateCache();
    }

    private void updateCache() {
        int pageNumber = 0;

        while (populateCacheWithDataPage(pageNumber, dataPageSize)) {
            pageNumber++;
        }

        lastRefreshTime = ZonedDateTime.now(ZoneOffset.UTC);
    }

    private boolean populateCacheWithDataPage(int page, int size) {
        CacheableDataPage<T> cacheableDataPage = loadDataPage(page, size, lastRefreshTime);
        cacheableDataPage.data().forEach(data -> getDataCache().put(data.getId(), data));
        return cacheableDataPage.pageNumber() < cacheableDataPage.pagesCount() - 1;
    }

    protected abstract CacheableDataPage<T> loadDataPage(int page, int size, ZonedDateTime lastRefreshTime);
}
