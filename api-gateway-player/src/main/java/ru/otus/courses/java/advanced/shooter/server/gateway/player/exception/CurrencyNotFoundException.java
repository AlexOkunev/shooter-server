package ru.otus.courses.java.advanced.shooter.server.gateway.player.exception;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(int id) {
        super("Currency not found: %d".formatted(id));
    }
}
