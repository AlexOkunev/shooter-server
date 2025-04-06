package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
