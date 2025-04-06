package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.exception;

public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
