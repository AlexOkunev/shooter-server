package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.inventory.outbox.ProductTradeIssuancePerformedMessage;

public interface ProductTradeProcessingService {
    void processMessage(ProductTradeIssuancePerformedMessage message);
}
