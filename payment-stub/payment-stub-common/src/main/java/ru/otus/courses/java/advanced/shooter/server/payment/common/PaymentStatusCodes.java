package ru.otus.courses.java.advanced.shooter.server.payment.common;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentStatusCodes {
    public static final int PROCESSING_CODE = 1;

    public static final int SUCCESS_CODE = 2;

    public static final int FAILED_CODE = 3;
}