package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.CacheableDataCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;

public interface ReferenceCurrencyCacheService extends CacheableDataCacheService<Integer, ReferenceCurrency> {
}
