package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.properties;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "payment-processing")
public class PaymentProcessingProperties {
    @Min(0)
    @Max(100)
    private final int failureRate = 0;

    @Positive
    private final int paymentSystemResponseDelayMsMax = 300;

    @Positive
    private final int processingTimeMsMax = 2_000;

    @Positive
    private final int batchSize = 20;

    @Positive
    private final int sendMessagesPeriodMs = 500;
}