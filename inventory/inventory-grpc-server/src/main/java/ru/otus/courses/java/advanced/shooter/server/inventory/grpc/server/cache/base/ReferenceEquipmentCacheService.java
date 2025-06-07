package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.base;

import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.service.CacheableDataCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;

public interface ReferenceEquipmentCacheService extends CacheableDataCacheService<ReferenceEquipmentId, ReferenceEquipment> {
}
