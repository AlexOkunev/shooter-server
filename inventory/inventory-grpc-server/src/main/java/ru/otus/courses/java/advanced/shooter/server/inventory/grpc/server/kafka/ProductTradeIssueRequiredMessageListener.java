package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.config.KafkaListenerConfig;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.market.outbox.ProductTradeIssueRequiredMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductTradeIssueRequiredMessageListener {

    private final PlayerInventoryTradeProcessingService playerInventoryTradeProcessingService;

    @KafkaListener(
            topics = "#{'${market-kafka-product-trade-issue-required.topic}'}",
            groupId = "#{'${market-kafka-product-trade-issue-required.group-id}'}",
            containerFactory = KafkaListenerConfig.MULTI_THREAD_PRODUCT_ISSUE_REQUIRED_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    public void listen(ProductTradeIssueRequiredMessage message) {
        log.info("Product trade issue required message received: {}", message);

        try {
            playerInventoryTradeProcessingService.processMessage(message);
        } catch (Exception e) {
            log.error("Error processing product trade issue required message: {}", message, e);
        }

        log.info("Product trade issue required message processed: {}", message);
    }
}