package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "payment-stub-grpc")
public class PaymentStubGrpcProperties {
    @NotBlank
    private String host;

    @Positive
    private int port;

    @Positive
    private int deadlineMs;
}
