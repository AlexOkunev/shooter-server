package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperationType {
    UNKNOWN(-1), ADMIN_GIVE(0), ADMIN_TAKE_AWAY(1), SPEND(2), BUY(3), GIVE_AWARD(4), SELL(5), HOLD(6);

    private final int code;

    public static OperationType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElse(OperationType.UNKNOWN);
    }
}