package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.job.PaymentResultSendJob;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.properties.PaymentProcessingProperties;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ScheduledTaskConfig implements SchedulingConfigurer {
    private final PaymentResultSendJob paymentResultSendJob;

    private final PaymentProcessingProperties paymentProcessingProperties;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addFixedRateTask(new FixedRateTask(
                paymentResultSendJob,
                Duration.ofMillis(paymentProcessingProperties.getSendMessagesPeriodMs()),
                Duration.ofMillis(paymentProcessingProperties.getSendMessagesPeriodMs())
        ));
    }
}
