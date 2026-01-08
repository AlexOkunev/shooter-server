package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AttachmentType {
    SCOPE(1),
    SUPPRESSOR(2),
    DAMPER(3);

    private final int code;

    public static AttachmentType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow();
    }
}
