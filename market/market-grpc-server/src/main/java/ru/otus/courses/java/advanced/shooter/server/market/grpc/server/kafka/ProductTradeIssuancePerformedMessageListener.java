package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.outbox.ProductTradeIssuancePerformedMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductTradeProcessingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductTradeIssuancePerformedMessageListener {

    private final ProductTradeProcessingService productTradeProcessingService;

    @KafkaListener(topics = "#{'${inventory-product-trade-issuance-performed-kafka.topic}'}", groupId = "#{'${inventory-product-trade-issuance-performed-kafka.group-id}'}")
    public void listen(ProductTradeIssuancePerformedMessage message) {
        log.info("Product trade issuance performed message received: {}", message);
        productTradeProcessingService.processMessage(message);
    }
}
