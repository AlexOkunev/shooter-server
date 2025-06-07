package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MoneyBundleTradeStatus {
    UNKNOWN(-1),
    CREATED(0),
    PAYMENT_SESSION_WAIT(1),
    PAYMENT_SESSION_CREATED(2),
    PAYMENT_PENDING(3),
    PAYMENT_DONE(4),
    MONEY_ISSUE_WAIT(5),
    MONEY_ISSUE_PENDING(6),
    MONEY_ISSUE_DONE(7),
    SUCCEEDED(8),
    FAILED(9);

    private final int code;

    public static MoneyBundleTradeStatus fromCode(int code) {
        return Arrays.stream(values())
                .filter(value -> value.getCode() == code)
                .findFirst()
                .orElse(MoneyBundleTradeStatus.UNKNOWN);
    }
}

