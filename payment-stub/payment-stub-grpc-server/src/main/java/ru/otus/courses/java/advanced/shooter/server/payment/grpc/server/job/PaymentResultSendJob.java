package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.properties.PaymentProcessingProperties;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.repository.PaymentRepository;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.PaymentService;

import java.time.ZonedDateTime;
import java.util.Random;

@Slf4j
@Component
public class PaymentResultSendJob implements Runnable {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final Pageable pageable;
    private final Random random;
    private final int failureRate;

    public PaymentResultSendJob(PaymentRepository paymentRepository, PaymentService paymentService,
                                PaymentProcessingProperties paymentProcessingProperties) {
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.pageable = PageRequest.of(0, paymentProcessingProperties.getBatchSize());
        this.random = new Random();
        this.failureRate = paymentProcessingProperties.getFailureRate();
    }

    @Override
    public void run() {
        Page<Payment> payments;

        do {
            payments = paymentRepository.findAllByProcessingFinishedTimestampBeforeAndStatus(
                    ZonedDateTime.now(),
                    PaymentStatus.PROCESSING,
                    pageable
            );

            if (!payments.getContent().isEmpty()) {
                log.info("Processing payments. Count: {}", payments.getNumberOfElements());
            }

            for (Payment payment : payments) {
                try {
                    paymentService.processPaymentResult(
                            payment,
                            random.nextInt(100) < failureRate
                                    ? PaymentStatus.FAILED
                                    : PaymentStatus.SUCCEEDED
                    );
                } catch (RuntimeException e) {
                    log.error("Payment processing failed. UUID: {}", payment.getPaymentUuid(), e);
                }
            }
        } while (!payments.isEmpty());
    }
}
