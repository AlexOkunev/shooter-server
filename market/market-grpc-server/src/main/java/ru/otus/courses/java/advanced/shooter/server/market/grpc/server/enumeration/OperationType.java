package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperationType {
    ADMIN_GIVE(1),
    ADMIN_TAKE_AWAY(2),
    SPEND(3),
    BUY(4),
    GIVE_AWARD(5),
    SYSTEM_PLAYER_ACCOUNT_INIT(6),
    ADMIN_PLAYER_ACCOUNT_INIT(7),
    REFUND(8);

    private final int code;

    public static OperationType fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown operation type code: " + code));
    }
}
