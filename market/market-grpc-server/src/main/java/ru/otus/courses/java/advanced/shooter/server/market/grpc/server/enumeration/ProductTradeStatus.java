package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductTradeStatus {
    ISSUE_EQUIPMENT_PENDING(5),
    SUCCEEDED(7),
    FAILED(8);

    private final int code;

    public static ProductTradeStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ProductTradeStatus code: " + code));
    }
}