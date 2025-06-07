package ru.otus.courses.java.advanced.shooter.server.common.utils.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
