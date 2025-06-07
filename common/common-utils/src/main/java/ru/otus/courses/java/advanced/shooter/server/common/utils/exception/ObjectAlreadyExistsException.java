package ru.otus.courses.java.advanced.shooter.server.common.utils.exception;

public class ObjectAlreadyExistsException extends RuntimeException {
    public ObjectAlreadyExistsException(String message) {
        super(message);
    }
}
