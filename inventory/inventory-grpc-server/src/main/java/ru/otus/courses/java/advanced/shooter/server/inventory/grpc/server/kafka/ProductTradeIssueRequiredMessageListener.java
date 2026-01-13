package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.config.KafkaErrorHandlerConfig;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.market.outbox.ProductTradeIssueRequiredMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductTradeIssueRequiredMessageListener {

    private final PlayerInventoryTradeProcessingService playerInventoryTradeProcessingService;

    @KafkaListener(
            topics = "#{'${market-kafka.topic}'}",
            groupId = "#{'${market-kafka.group-id}'}"
    )
    public void listen(ProductTradeIssueRequiredMessage message) {
        log.info("Produce trade issue required message received: {}", message);
        playerInventoryTradeProcessingService.processMessage(message);
    }
}