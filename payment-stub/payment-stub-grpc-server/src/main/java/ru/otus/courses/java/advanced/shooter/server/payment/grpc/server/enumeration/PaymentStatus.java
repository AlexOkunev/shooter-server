package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    PROCESSING(0),
    SUCCEEDED(1),
    FAILED(2);

    private final int code;

    public static PaymentStatus getByCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown payment status code: " + code));
    }
}
