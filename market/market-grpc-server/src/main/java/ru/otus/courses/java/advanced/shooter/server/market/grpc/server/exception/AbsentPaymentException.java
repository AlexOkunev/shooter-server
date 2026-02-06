package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.exception;

public class AbsentPaymentException extends RuntimeException {
    public AbsentPaymentException(String message) {
        super(message);
    }
}
