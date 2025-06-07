package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum InventoryEquipmentType {
    UNKNOWN(EquipmentType.UNKNOWN.getNumber()),
    GUN(EquipmentType.GUN.getNumber()),
    GRENADE(EquipmentType.GRENADE.getNumber()),
    ATTACHMENT(EquipmentType.ATTACHMENT.getNumber()),
    AMMUNITION(EquipmentType.AMMUNITION.getNumber());

    private final int code;

    public static InventoryEquipmentType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElse(InventoryEquipmentType.UNKNOWN);
    }
}

