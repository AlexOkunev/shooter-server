package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductTradeStatus {
    UNKNOWN(-1),
    CREATED(0),
    WRITE_OFF_WAIT(1),
    WRITE_OFF_PENDING(2),
    WRITE_OFF_DONE(3),
    ISSUE_EQUIPMENT_PENDING(4),
    ISSUE_EQUIPMENT_DONE(5),
    SUCCEEDED(6),
    FAILED(7);

    private final int code;

    public static ProductTradeStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElse(ProductTradeStatus.UNKNOWN);
    }
}

