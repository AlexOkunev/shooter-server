package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ProductTradeStatus {
    CREATED(1),
    WRITE_OFF_WAIT(2),
    WRITE_OFF_PENDING(3),
    WRITE_OFF_DONE(4),
    ISSUE_EQUIPMENT_PENDING(5),
    ISSUE_EQUIPMENT_DONE(6),
    SUCCEEDED(7),
    FAILED(8),
    TIMEOUT(9);

    private final int code;

    public static ProductTradeStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ProductTradeStatus code: " + code));
    }
}

//TODO!!! maybe reduce number of statuses