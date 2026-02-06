package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.kafka.types.Currency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceCurrencyCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.config.KafkaListenerConfig;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ReferenceCurrencyService;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyMessageListener {

    private final ReferenceCurrencyService referenceCurrencyService;

    @KafkaListener(
            topics = "${currency-kafka.topic}",
            groupId = "${currency-kafka.group-id}",
            containerFactory = KafkaListenerConfig.SINGLE_THREAD_CURRENCY_KAFKA_LISTENER_CONTAINER_FACTORY
    )
    public void handle(Currency message) {
        log.info("[currency] Currency received: {}", message);

        SaveReferenceCurrencyCommand command = SaveReferenceCurrencyCommand.builder()
                .id(message.getId())
                .enabled(message.getEnabled())
                .canBeBought(message.getCanBeBought())
                .name(message.getName())
                .build();

        saveReferenceCurrency(command);
    }

    private void saveReferenceCurrency(SaveReferenceCurrencyCommand command) {
        try {
            ReferenceCurrency currency = referenceCurrencyService.save(command);
            log.info("[currency] Reference currency {} saved successfully", currency.getId());
        } catch (Exception e) {
            log.error("[currency] Failed to save reference currency: {}", command, e);
            throw e;
        }
    }
}
