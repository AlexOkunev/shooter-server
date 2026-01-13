package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean;

import lombok.Builder;
import lombok.Value;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

import java.util.List;

@Value
@Builder
public class PlayerInventoryItemFilterParams {
    Boolean enabled;

    @Builder.Default
    List<InventoryEquipmentType> types = List.of();
}
