package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MoneyBundleTradeStatus {
    CREATED(1),
    PAYMENT_WAIT(2),
    PAYMENT_PENDING(3),
    SUCCEEDED(4),
    FAILED(5);

    private final int code;

    public static MoneyBundleTradeStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown MoneyBundleTradeStatus code: " + code));
    }
}

