package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.CacheableDataCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;

public interface ReferenceEquipmentCacheService extends CacheableDataCacheService<ReferenceEquipmentId, ReferenceEquipment> {
}
