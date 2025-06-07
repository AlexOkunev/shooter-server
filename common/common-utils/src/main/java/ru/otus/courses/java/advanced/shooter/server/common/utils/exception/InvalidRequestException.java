package ru.otus.courses.java.advanced.shooter.server.common.utils.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
