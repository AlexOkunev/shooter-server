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

    private final PaymentProcessingProperties paymentProcessingProperties;

    private final Pageable pageable;

    private final Random random;

    public PaymentResultSendJob(PaymentRepository paymentRepository, PaymentService paymentService,
                                PaymentProcessingProperties paymentProcessingProperties) {
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.paymentProcessingProperties = paymentProcessingProperties;
        this.pageable = PageRequest.of(0, paymentProcessingProperties.getBatchSize());
        random = new Random();
    }

    @Override
    public void run() {
        Page<Payment> payments;

        do {
            payments = paymentRepository.findAllByProcessingFinishedTimestampBeforeAndStatus(ZonedDateTime.now(),
                    PaymentStatus.PROCESSING, pageable);
            log.info("Processing payments. Count: {}", payments.getNumberOfElements());

            for (Payment payment : payments) {
                try {
                    PaymentStatus paymentStatus = random.nextInt(100) < paymentProcessingProperties.getFailureRate() ?
                            PaymentStatus.FAILED : PaymentStatus.SUCCEEDED;
                    paymentService.processPaymentResult(payment, paymentStatus);
                } catch (RuntimeException e) {
                    log.error("Payment processing failed. UUID: {}", payment.getPaymentUuid(), e);
                }
            }
        } while (!payments.isEmpty());
    }
}
