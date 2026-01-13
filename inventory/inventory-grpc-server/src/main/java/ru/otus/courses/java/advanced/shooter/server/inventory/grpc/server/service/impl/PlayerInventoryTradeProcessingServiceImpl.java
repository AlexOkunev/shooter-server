package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ProcessedExternalMessage;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain.ExternalMessageMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.ProcessedExternalMessageRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryTradeProcessingService;
import ru.otus.courses.java.advanced.shooter.server.market.outbox.ProductTradeIssueRequiredMessage;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInventoryTradeProcessingServiceImpl implements PlayerInventoryTradeProcessingService {

    private final ProcessedExternalMessageRepository processedExternalMessageRepository;
    private final PlayerInventoryService playerInventoryService;
    private final ExternalMessageMapper externalMessageMapper;

    @Override
    @Transactional
    public void processMessage(ProductTradeIssueRequiredMessage message) {
        UUID messageUuid = UUID.fromString(message.getMessageUuid());
        UUID tradeUuid = UUID.fromString(message.getTradeUuid());

        log.info("Processing message with UUID: {}, trade UUID: {}", messageUuid, tradeUuid);

        if (processedExternalMessageRepository.existsByUuid(messageUuid)) {
            log.error("External message with UUID {} has already been processed", messageUuid);
        }

        playerInventoryService.buyEquipment(PlayerEquipmentOperationCommand.builder()
//                .playerUuid(UUID.fromString(message.getPlayerId())) TODO!!! fix
                .equipmentId(message.getEquipmentId())
                .equipmentType(InventoryEquipmentType.fromCode(message.getEquipmentType()))
                .amount(message.getEquipmentAmount())
                .build()
        );

        ProcessedExternalMessage processedExternalMessage = externalMessageMapper.map(message);
        processedExternalMessageRepository.save(processedExternalMessage);

        log.info("Saved processed external message: {}", messageUuid);
    }
}

//TODO кэш обработанных сообщений