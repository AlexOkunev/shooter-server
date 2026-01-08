package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum GunType {
    PISTOL(1),
    SUBMACHINE_GUN(2),
    ASSAULT_RIFLE(3),
    SCATTERSHOT(4),
    MACHINE_GUN(5),
    PRECISION_RIFLE(6);

    private final int code;

    public static GunType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow();
    }
}
