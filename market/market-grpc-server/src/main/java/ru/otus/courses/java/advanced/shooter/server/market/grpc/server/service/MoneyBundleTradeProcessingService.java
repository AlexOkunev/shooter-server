package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.payment.outbox.ProcessedPaymentMessage;

public interface MoneyBundleTradeProcessingService {
    void processMessage(ProcessedPaymentMessage message);
}
