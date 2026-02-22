package ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.market;

public enum MoneyBundleTradeStatus {
    CREATED,
    PAYMENT_CREATION_WAIT,
    PAYMENT_PENDING,
    SUCCEEDED,
    FAILED,
    TIMEOUT;
}
