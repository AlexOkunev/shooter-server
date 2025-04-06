package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.EquipmentCache;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.EquipmentCacheService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentCacheServiceImpl implements EquipmentCacheService {
    private final List<EquipmentCache> equipmentCacheList;

    @Override
    public Equipment getFromCache(EquipmentType type, int id) {
        return equipmentCacheList.stream()
                .filter(equipmentCache -> equipmentCache.supports(type))
                .findFirst()
                .flatMap(equipmentCache -> equipmentCache.getById(id))
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s (id = %d) not found".formatted(type.name(), id)));
    }

    @Override
    public Optional<Equipment> getOptionalFromCache(EquipmentType type, int id) {
        return equipmentCacheList.stream()
                .filter(equipmentCache -> equipmentCache.supports(type))
                .findFirst()
                .flatMap(equipmentCache -> equipmentCache.getById(id));
    }
}
