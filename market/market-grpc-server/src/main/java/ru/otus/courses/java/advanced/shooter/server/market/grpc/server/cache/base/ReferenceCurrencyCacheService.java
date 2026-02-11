package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.RefreshableCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;

public interface ReferenceCurrencyCacheService extends RefreshableCacheService<Integer, ReferenceCurrency> {
}
