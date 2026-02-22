package ru.otus.courses.java.advanced.shooter.server.gateway.player.exception;

public class AmmunitionNotFoundException extends RuntimeException {
    public AmmunitionNotFoundException(int id) {
        super("Ammunition not found: %d".formatted(id));
    }
}
