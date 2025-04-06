package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
