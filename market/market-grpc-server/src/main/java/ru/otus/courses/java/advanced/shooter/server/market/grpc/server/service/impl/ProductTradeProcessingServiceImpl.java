package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.inventory.outbox.ProductTradeIssuancePerformedMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProcessedExternalMessage;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProcessedExternalMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.ProductTradeRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductTradeProcessingService;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTradeProcessingServiceImpl implements ProductTradeProcessingService {

    private final ProductTradeRepository productTradeRepository;
    private final ProcessedExternalMessageRepository processedExternalMessageRepository;

    @Override
    @Transactional
    public void processMessage(ProductTradeIssuancePerformedMessage message) {
        log.info("Process product trade issuance performed message {}. Player uuid {}. Trade uuid {}", message.getMessageUuid(), message.getPlayerUuid(), message.getTradeUuid());

        UUID messageUuid = UUID.fromString(message.getMessageUuid());
        UUID playerUuid = UUID.fromString(message.getPlayerUuid());
        UUID tradeUuid = UUID.fromString(message.getTradeUuid());

        //TODO использовать кеш сообщений. но не загружать в него старые сообщения, только те которые были явно запрошены. кеш чистить
        if (processedExternalMessageRepository.existsByMessageUUID(messageUuid)) {
            log.error("External message with uuid {} has already been processed", message.getMessageUuid());
            return;
        }

        ProductTrade productTrade = productTradeRepository.findByPlayerUuidAndUuid(playerUuid, tradeUuid)
                .orElseThrow(() -> new IllegalArgumentException("Player %s product trade %s not found"
                        .formatted(message.getPlayerUuid(), message.getTradeUuid())
                ));

        if(productTrade.getStatus() == ProductTradeStatus.SUCCEEDED) {
            log.error("Trade {} is already in status {}", productTrade.getUuid(), productTrade.getStatus());
            ProcessedExternalMessage processedExternalMessage = saveProcessedExternalMessage(message, messageUuid);
            log.info("Save processed external message: {}", processedExternalMessage.getMessageUUID());
        }

        productTrade.setUpdatedTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        productTrade.setStatus(message.getSuccess() ? ProductTradeStatus.SUCCEEDED : ProductTradeStatus.FAILED);
        productTradeRepository.save(productTrade);

        log.info("Saved trade {} status {} for message {}", productTrade.getUuid(), productTrade.getStatus(), messageUuid);

        ProcessedExternalMessage processedExternalMessage = saveProcessedExternalMessage(message, messageUuid);

        log.info("Save processed external message: {}", processedExternalMessage.getMessageUUID());
    }

    private ProcessedExternalMessage saveProcessedExternalMessage(ProductTradeIssuancePerformedMessage message, UUID messageUuid) {
        ProcessedExternalMessage processedExternalMessage = new ProcessedExternalMessage();
        processedExternalMessage.setMessageUUID(messageUuid);
        processedExternalMessage.setMessageCreatedTimestamp(ZonedDateTime.ofInstant(message.getCreatedTimestamp(), ZoneOffset.UTC));
        processedExternalMessage.setMessageProcessedTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        processedExternalMessage = processedExternalMessageRepository.save(processedExternalMessage);
        return processedExternalMessage;
    }
}
