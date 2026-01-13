package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.outbox.ProductTradeIssueRequiredMessage;

public interface PlayerInventoryTradeProcessingService {
    void processMessage(ProductTradeIssueRequiredMessage message);
}
