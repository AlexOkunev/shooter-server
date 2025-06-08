package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.properties.PaymentProcessingProperties;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service.PaymentSystemStubService;

import java.util.Random;

@Slf4j
@Service
public class PaymentSystemStubServiceImpl implements PaymentSystemStubService {
    private final PaymentProcessingProperties paymentProcessingProperties;

    private final Random random;

    private final RandomStringUtils randomStringUtils;

    public PaymentSystemStubServiceImpl(PaymentProcessingProperties paymentProcessingProperties) {
        this.paymentProcessingProperties = paymentProcessingProperties;
        this.random = new Random();
        this.randomStringUtils = RandomStringUtils.secure();
    }

    @Override
    @SneakyThrows
    public String getSession(Integer playerId, String playerEmail, int rublesAmount, String tradeUuid) {
        log.info("Create session for playerId: {}, playerEmail: {}, rublesAmount: {}, tradeUuid: {}", playerId, playerEmail, rublesAmount, tradeUuid);
        int delay = random.nextInt(paymentProcessingProperties.getPaymentSystemResponseDelayMsMax());
        Thread.sleep(delay);
        String sessionId = randomStringUtils.next(15, true, true);
        log.info("Created session for playerId: {}, playerEmail: {}, rublesAmount: {}, tradeUuid: {}", playerId, playerEmail, rublesAmount, tradeUuid);
        return sessionId;
    }

    @Override
    @SneakyThrows
    public String getPublicToken(String session) {
        log.info("Get public token for session: {}", session);
        int delay = random.nextInt(paymentProcessingProperties.getPaymentSystemResponseDelayMsMax());
        Thread.sleep(delay);
        String token = randomStringUtils.next(30, true, true);
        log.info("Got public token for session: {}", session);
        return token;
    }
}
