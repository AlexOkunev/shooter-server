package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.PaymentProcessedMessage;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper.PaymentMapper;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper.PaymentMessageMapper;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.properties.PaymentProcessingProperties;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.repository.PaymentProcessedMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.repository.PaymentRepository;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.PaymentService;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.PaymentSystemStubService;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final PaymentProcessedMessageRepository paymentProcessedMessageRepository;

    private final PaymentSystemStubService paymentSystemStubService;

    private final PaymentMapper paymentMapper;

    private final PaymentMessageMapper paymentMessageMapper;

    private final PaymentProcessingProperties paymentProcessingProperties;

    private final Random random;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              PaymentProcessedMessageRepository paymentProcessedMessageRepository,
                              PaymentSystemStubService paymentSystemStubService,
                              PaymentMapper paymentMapper,
                              PaymentMessageMapper paymentMessageMapper,
                              PaymentProcessingProperties paymentProcessingProperties) {
        this.paymentRepository = paymentRepository;
        this.paymentProcessedMessageRepository = paymentProcessedMessageRepository;
        this.paymentSystemStubService = paymentSystemStubService;
        this.paymentMapper = paymentMapper;
        this.paymentMessageMapper = paymentMessageMapper;
        this.paymentProcessingProperties = paymentProcessingProperties;
        this.random = new Random();
    }

    @Override
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("Creating payment. Request: {}", request);

        validateCreateRequest(request);

        Optional<Payment> existingPayment = paymentRepository.findByTradeUuid(UUID.fromString(request.getTradeUuid()));
        if (existingPayment.isPresent()) {
            return paymentMapper.toResponse(existingPayment.get());
        }

        String session = paymentSystemStubService.getSession(request.getPlayerId(), request.getPlayerEmail(), request.getRublesAmount(), request.getTradeUuid());
        String publicToken = paymentSystemStubService.getPublicToken(session);
        Payment payment = paymentMapper.toEntity(request, session, publicToken);

        ZonedDateTime processingFinishedTimestamp = ZonedDateTime.now().plus(
                random.nextInt(paymentProcessingProperties.getProcessingTimeMsMax()), ChronoUnit.MILLIS);
        payment.setProcessingFinishedTimestamp(processingFinishedTimestamp);

        payment = paymentRepository.save(payment);

        log.info("Created payment: {}", payment);

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public void processPaymentResult(Payment payment, PaymentStatus status) {
        log.info("Processing payment result. UUID: {}, status: {}", payment.getPaymentUuid(), status);

        payment.setStatus(status);
        paymentRepository.save(payment);
        log.info("Payment processed: {}", payment);

        PaymentProcessedMessage paymentProcessedMessage = paymentMessageMapper.toEntity(payment);
        paymentProcessedMessageRepository.save(paymentProcessedMessage);
        log.info("Payment processed message saved: {}", paymentProcessedMessage);
    }

    private static void validateCreateRequest(CreatePaymentRequest request) {
        if (StringUtils.isBlank(request.getPlayerEmail())) {
            throw new IllegalArgumentException("Player email is blank");
        }

        if (request.getRublesAmount() <= 0) {
            throw new IllegalArgumentException("Rubles amount is not positive");
        }

        if (StringUtils.isBlank(request.getTradeUuid())) {
            throw new IllegalArgumentException("Trade uuid is blank");
        }
    }
}
