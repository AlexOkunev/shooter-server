package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInventoryItemId {
    private int playerId;

    private EquipmentType equipmentType;

    private int equipmentId;
}
