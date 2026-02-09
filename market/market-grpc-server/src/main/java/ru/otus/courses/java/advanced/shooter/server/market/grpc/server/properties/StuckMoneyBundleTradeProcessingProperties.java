package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "stuck-money-bundle-trade-processing")
public class StuckMoneyBundleTradeProcessingProperties {

    private boolean enabled;

    @NotNull
    private Duration period = Duration.ofSeconds(10);

    @Valid
    @NotNull
    private PaymentCreationWaitProcessingProperties paymentCreationWaitProcessing = new PaymentCreationWaitProcessingProperties();

    @Valid
    @NotNull
    private StuckMoneyBundleTradeProcessingProperties.PaymentPendingProcessingProperties paymentPendingProcessing = new PaymentPendingProcessingProperties();

    @Data
    public static class PaymentCreationWaitProcessingProperties {

        private boolean enabled = true;

        @NotNull
        private Duration maxStuckPeriod = Duration.ofSeconds(30);
    }

    @Data
    public static class PaymentPendingProcessingProperties {

        private boolean enabled = true;

        @NotNull
        private Duration maxStuckPeriod = Duration.ofMinutes(30);
    }
}
