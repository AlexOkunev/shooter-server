package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum EquipmentType {
    UNKNOWN(-1), GUN(0), GRENADE(1), ATTACHMENT(2), AMMUNITION(3);

    private final int code;

    public static EquipmentType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElse(EquipmentType.UNKNOWN);
    }
}

