package ru.otus.courses.java.advanced.shooter.server.gateway.player.exception;

public class GunNotFoundException extends RuntimeException {
    public GunNotFoundException(int id) {
        super("Gun not found: %d".formatted(id));
    }
}
