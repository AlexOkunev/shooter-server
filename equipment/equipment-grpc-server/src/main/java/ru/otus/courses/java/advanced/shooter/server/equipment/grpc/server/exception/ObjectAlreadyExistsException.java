package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.exception;

public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
