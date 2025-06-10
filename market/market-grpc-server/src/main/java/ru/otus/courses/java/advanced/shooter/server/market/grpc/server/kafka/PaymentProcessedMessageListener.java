package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.payment.outbox.ProcessedPaymentMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProcessedMessageListener {

    private final MoneyBundleTradeProcessingService moneyBundleTradeProcessingService;

    @KafkaListener(topics = "#{'${payment-kafka.topic}'}", groupId = "#{'${payment-kafka.group-id}'}")
    public void listen(ProcessedPaymentMessage message) {
        log.info("Payment processed message received: {}", message);
        moneyBundleTradeProcessingService.processMessage(message);
    }
}
