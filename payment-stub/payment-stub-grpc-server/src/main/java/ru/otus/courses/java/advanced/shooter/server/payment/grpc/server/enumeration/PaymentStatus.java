package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.otus.courses.java.advanced.shooter.server.payment.common.PaymentStatusCodes;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
    PROCESSING(PaymentStatusCodes.PROCESSING_CODE),
    SUCCEEDED(PaymentStatusCodes.SUCCESS_CODE),
    FAILED(PaymentStatusCodes.FAILED_CODE);

    private final int code;

    public static PaymentStatus getByCode(int code) {
        return Arrays.stream(values())
                .filter(status -> status.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown payment status code: " + code));
    }
}
