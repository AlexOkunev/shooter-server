package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;

import java.util.Optional;

public interface EquipmentCacheService {
    Equipment getFromCache(EquipmentType type, int id);

    Optional<Equipment> getOptionalFromCache(EquipmentType type, int id);
}
