package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.outbox.ProductTradeIssuancePerformedMessage;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain.ExternalMessageMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.ProcessedExternalMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.ProductTradeIssuancePerformedMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.market.outbox.ProductTradeIssueRequiredMessage;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInventoryTradeProcessingServiceImpl implements PlayerInventoryTradeProcessingService {

    private final PlayerInventoryService playerInventoryService;
    private final ProcessedExternalMessageRepository processedExternalMessageRepository;
    private final ProductTradeIssuancePerformedMessageRepository productTradeIssuancePerformedMessageRepository;
    private final ExternalMessageMapper externalMessageMapper;

    @Override
    @Transactional
    public void processMessage(ProductTradeIssueRequiredMessage message) {
        UUID messageUuid = UUID.fromString(message.getMessageUuid());
        UUID tradeUuid = UUID.fromString(message.getTradeUuid());
        UUID playerUuid = UUID.fromString(message.getPlayerUuid());

        log.info("Processing message with UUID: {}, trade UUID: {}, player UUID: {}", messageUuid, tradeUuid, playerUuid);

        if (processedExternalMessageRepository.existsByUuid(messageUuid)) {
            log.error("External message with UUID {} has already been processed", messageUuid);
            return;
        }

        if (productTradeIssuancePerformedMessageRepository.existsByPlayerUuidAndTradeUuidAndSuccess(playerUuid, tradeUuid, true)) {
            log.error("Player {} has already been issued equipment for trade {}. Message {}", playerUuid, tradeUuid, messageUuid);
            processedExternalMessageRepository.save(externalMessageMapper.map(message));
            return;
        }

        boolean success = playerInventoryService.buyEquipment(PlayerEquipmentOperationCommand.builder()
                .playerUuid(playerUuid)
                .equipmentId(message.getEquipmentId())
                .equipmentType(InventoryEquipmentType.fromCode(message.getEquipmentType()))
                .amount(message.getEquipmentAmount())
                .build()
        );

        productTradeIssuancePerformedMessageRepository.save(ProductTradeIssuancePerformedMessage.builder()
                .playerUuid(playerUuid)
                .tradeUuid(tradeUuid)
                .messageUuid(UUID.randomUUID())
                .success(success)
                .build()
        );

        processedExternalMessageRepository.save(externalMessageMapper.map(message));

        log.info("Saved processed external message: {}", messageUuid);
    }
}

//TODO кэш обработанных сообщений