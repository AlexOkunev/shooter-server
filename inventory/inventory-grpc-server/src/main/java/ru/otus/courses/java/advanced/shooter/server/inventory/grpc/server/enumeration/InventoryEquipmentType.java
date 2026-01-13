package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum InventoryEquipmentType {
    GUN(EquipmentType.GUN_VALUE),
    GRENADE(EquipmentType.GRENADE_VALUE),
    ATTACHMENT(EquipmentType.ATTACHMENT_VALUE),
    AMMUNITION(EquipmentType.AMMUNITION_VALUE);

    private final int code;

    public static InventoryEquipmentType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown equipment type code: " + code));
    }
}

