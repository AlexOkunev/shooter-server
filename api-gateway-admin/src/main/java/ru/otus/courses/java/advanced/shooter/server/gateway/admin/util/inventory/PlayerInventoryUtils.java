package ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.inventory;

import lombok.experimental.UtilityClass;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemInfo;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemInfo;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PlayerInventoryUtils {
    public static Set<Integer> getEquipmentIds(InitialPlayerInventoryItemsPage inventoryPage, EquipmentType equipmentType) {
        return inventoryPage.getDataList()
                .stream()
                .filter(item -> item.getEquipmentType() == equipmentType)
                .map(InitialPlayerInventoryItemInfo::getEquipmentId)
                .collect(Collectors.toSet());
    }

    public static Set<Integer> getEquipmentIds(PlayerInventoryItemsPage inventoryPage, EquipmentType equipmentType) {
        return inventoryPage.getDataList()
                .stream()
                .filter(item -> item.getEquipmentType() == equipmentType)
                .map(PlayerInventoryItemInfo::getEquipmentId)
                .collect(Collectors.toSet());
    }

    public static Set<Integer> getEquipmentIds(PlayerInventoryLogPage logPage, EquipmentType equipmentType) {
        return logPage.getDataList()
                .stream()
                .filter(item -> item.getEquipmentType() == equipmentType)
                .map(PlayerInventoryLogEntry::getEquipmentId)
                .collect(Collectors.toSet());
    }
}
