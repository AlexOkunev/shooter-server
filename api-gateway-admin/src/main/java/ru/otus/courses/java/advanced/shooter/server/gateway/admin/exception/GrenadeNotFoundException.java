package ru.otus.courses.java.advanced.shooter.server.gateway.admin.exception;

public class GrenadeNotFoundException extends RuntimeException {
    public GrenadeNotFoundException(int id) {
        super("Grenade not found: %d".formatted(id));
    }
}
