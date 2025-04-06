package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;

import java.util.Optional;

public interface EquipmentCache {
    void put(Equipment equipment);

    Optional<Equipment> getById(int id);

    boolean loadDataPage(int page, int size);

    boolean supports(EquipmentType equipmentType);
}
