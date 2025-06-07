package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.CacheableDataCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;

public interface MoneyBundleCacheService extends CacheableDataCacheService<Integer, MoneyBundle> {
}
