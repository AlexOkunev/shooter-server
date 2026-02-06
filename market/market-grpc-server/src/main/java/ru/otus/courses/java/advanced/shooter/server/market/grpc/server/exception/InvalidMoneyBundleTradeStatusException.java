package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.exception;

public class InvalidMoneyBundleTradeStatusException extends RuntimeException {
    public InvalidMoneyBundleTradeStatusException(String message) {
        super(message);
    }
}
