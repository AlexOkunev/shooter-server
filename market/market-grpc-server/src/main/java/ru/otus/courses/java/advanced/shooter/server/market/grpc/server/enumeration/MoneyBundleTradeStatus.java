package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MoneyBundleTradeStatus {
    UNKNOWN(-1),
    CREATED(0),
    PAYMENT_PENDING(1),
    SUCCEEDED(2),
    FAILED(3);

    private final int code;

    public static MoneyBundleTradeStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElse(MoneyBundleTradeStatus.UNKNOWN);
    }
}

