package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectAlreadyExistsException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerInventoryItemFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.*;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain.PlayerInventoryItemMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain.PlayerInventoryLogEntryMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.InitialPlayerInventoryItemRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.PlayerInventoryItemRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.PlayerInventoryLogRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification.PlayerInventoryItemSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PlayerInventoryServiceImpl implements PlayerInventoryService {
    private final PlayerInventoryItemRepository playerInventoryItemRepository;
    private final PlayerInventoryLogRepository playerInventoryLogRepository;
    private final InitialPlayerInventoryItemRepository initialPlayerInventoryItemRepository;
    private final PlayerInventoryItemMapper playerInventoryItemMapper;
    private final PlayerInventoryLogEntryMapper playerInventoryLogEntryMapper;
    private final ReferenceEquipmentCacheService equipmentCacheService;

    @Override
    public Page<PlayerInventoryItem> getPlayerInventoryItemsPage(
            @NotNull UUID playerUuid,
            @NotNull Pageable pageable,
            @NotNull PlayerInventoryItemFilterParams filterParams
    ) {
        Specification<PlayerInventoryItem> specification = getSpecification(playerUuid, filterParams);
        return playerInventoryItemRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void initializePlayerInventory(@NotNull UUID playerUuid) {
        playerInventoryItemRepository.findFirstByIdPlayerUuid(playerUuid)
                .ifPresent(item -> {
                    throw new ObjectAlreadyExistsException("Player %s inventory is already initialized".formatted(playerUuid));
                });

        List<PlayerInventoryItem> inventoryItems = initialPlayerInventoryItemRepository.findAllByEnabledIsTrue().stream()
                .map(initialItem -> playerInventoryItemMapper.toEntity(initialItem, playerUuid))
                .toList();

        List<PlayerInventoryLogEntry> logEntries = inventoryItems.stream()
                .map(playerInventoryLogEntryMapper::toEntityForInitialize)
                .peek(logEntry -> logEntry.setOperationType(OperationType.ADMIN_INVENTORY_INIT))
                .toList();

        playerInventoryItemRepository.saveAll(inventoryItems);
        playerInventoryLogRepository.saveAll(logEntries);
    }

    @Override
    @Transactional
    public void initializePlayerInventoryBySystemEvent(@NotNull UUID playerUuid) {
        if (playerInventoryItemRepository.existsByIdPlayerUuid(playerUuid)) {
            log.info("Player {} inventory is already initialized", playerUuid);
            return;
        }

        List<PlayerInventoryItem> inventoryItems = initialPlayerInventoryItemRepository.findAllByEnabledIsTrue().stream()
                .map(initialItem -> playerInventoryItemMapper.toEntity(initialItem, playerUuid))
                .toList();

        List<PlayerInventoryLogEntry> logEntries = inventoryItems.stream()
                .map(playerInventoryLogEntryMapper::toEntityForInitialize)
                .peek(logEntry -> logEntry.setOperationType(OperationType.SYSTEM_INVENTORY_INIT))
                .toList();

        playerInventoryItemRepository.saveAll(inventoryItems);
        playerInventoryLogRepository.saveAll(logEntries);
    }

    @Override
    @Transactional
    public void giveEquipment(@Valid @NotNull PlayerEquipmentOperationCommand command) {
        log.info("Give equipment {} (ID={}) for player with UUID {}",
                command.getEquipmentType().name(), command.getEquipmentId(), command.getPlayerUuid());

        ReferenceEquipmentId referenceEquipmentId = new ReferenceEquipmentId(command.getEquipmentType(), command.getEquipmentId());

        ReferenceEquipment equipment = equipmentCacheService.getById(referenceEquipmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(
                        referenceEquipmentId.getEquipmentType().name(), referenceEquipmentId.getEquipmentId())));

        PlayerInventoryItemId playerInventoryItemId = new PlayerInventoryItemId(command.getPlayerUuid(), equipment.getId());
        PlayerInventoryItem playerInventoryItem = playerInventoryItemRepository.findById(playerInventoryItemId)
                .orElse(playerInventoryItemMapper.toEntity(command));

        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);

        playerInventoryItem.setAmount(playerInventoryItem.getAmount() + command.getAmount());

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.ADMIN_GIVE);

        playerInventoryItemRepository.save(playerInventoryItem);
        playerInventoryLogRepository.save(logEntry);

        log.info("Equipment {} (ID={}) given to player with UUID {} successfully",
                command.getEquipmentType().name(), command.getEquipmentId(), command.getPlayerUuid());
    }

    @Override
    @Transactional
    public boolean buyEquipment(@NotNull UUID tradeUuid, @Valid @NotNull PlayerEquipmentOperationCommand command) {
        log.info("Buy equipment {} (ID={}) for player with UUID {}. Trade UUID {}",
                command.getEquipmentType().name(), command.getEquipmentId(), command.getPlayerUuid(), tradeUuid);

        ReferenceEquipmentId referenceEquipmentId = new ReferenceEquipmentId(command.getEquipmentType(), command.getEquipmentId());

        Optional<ReferenceEquipment> equipmentOptional = equipmentCacheService.getById(referenceEquipmentId);
        if (equipmentOptional.isEmpty()) {
            log.error("Equipment {} (ID={}) not found", command.getEquipmentType().name(), command.getEquipmentId());
            return false;
        }

        ReferenceEquipment equipment = equipmentOptional.get();

        PlayerInventoryItemId playerInventoryItemId = new PlayerInventoryItemId(command.getPlayerUuid(), equipment.getId());
        PlayerInventoryItem playerInventoryItem = playerInventoryItemRepository.findById(playerInventoryItemId)
                .orElse(playerInventoryItemMapper.toEntity(command));

        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);
        logEntry.setOperationUuid(tradeUuid);

        playerInventoryItem.setAmount(playerInventoryItem.getAmount() + command.getAmount());

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.BUY);

        playerInventoryItemRepository.save(playerInventoryItem);
        playerInventoryLogRepository.save(logEntry);

        log.info("Equipment {} (ID={}) bought buy player with UUID {} successfully",
                command.getEquipmentType().name(), command.getEquipmentId(), command.getPlayerUuid());

        return true;
    }

    @Override
    @Transactional
    public void takeAwayEquipment(@Valid @NotNull PlayerEquipmentOperationCommand command) {
        ReferenceEquipmentId referenceEquipmentId = new ReferenceEquipmentId(command.getEquipmentType(), command.getEquipmentId());

        ReferenceEquipment equipment = equipmentCacheService.getById(referenceEquipmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(
                        referenceEquipmentId.getEquipmentType().name(), referenceEquipmentId.getEquipmentId())));

        PlayerInventoryItem playerInventoryItem = getPlayerInventoryItem(command.getPlayerUuid(), equipment.getId());
        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);

        validateAmountSufficiency(playerInventoryItem, command);
        playerInventoryItem.setAmount(playerInventoryItem.getAmount() - command.getAmount());

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.ADMIN_TAKE_AWAY);

        playerInventoryItemRepository.save(playerInventoryItem);
        playerInventoryLogRepository.save(logEntry);
    }

    @Override
    @Transactional
    public void spendEquipment(@Valid @NotNull PlayerEquipmentOperationCommand command) {
        ReferenceEquipmentId referenceEquipmentId = new ReferenceEquipmentId(command.getEquipmentType(), command.getEquipmentId());

        ReferenceEquipment equipment = equipmentCacheService.getById(referenceEquipmentId)
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(
                        referenceEquipmentId.getEquipmentType().name(), referenceEquipmentId.getEquipmentId())));

        if (!equipment.isEnabled()) {
            log.error("Equipment {} (ID = {}) is not enabled", equipment.getId().getEquipmentType().name(),
                    equipment.getId().getEquipmentId());
            throw new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(
                    equipment.getId().getEquipmentType().name(), equipment.getId().getEquipmentId()));
        }

        PlayerInventoryItem playerInventoryItem = getPlayerInventoryItem(command.getPlayerUuid(), equipment.getId());
        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);

        validateAmountSufficiency(playerInventoryItem, command);
        playerInventoryItem.setAmount(playerInventoryItem.getAmount() - command.getAmount());

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.SPEND);

        playerInventoryItemRepository.save(playerInventoryItem);
        playerInventoryLogRepository.save(logEntry);
    }

    private PlayerInventoryItem getPlayerInventoryItem(UUID playerUuid, ReferenceEquipmentId referenceEquipmentId) {
        PlayerInventoryItemId playerInventoryItemId = new PlayerInventoryItemId(playerUuid, referenceEquipmentId);

        return playerInventoryItemRepository.findById(playerInventoryItemId)
                .orElseThrow(() -> new ObjectNotFoundException("Player %s inventory item (%s with id %s) not found".formatted(
                        playerUuid,
                        referenceEquipmentId.getEquipmentType().name(),
                        referenceEquipmentId.getEquipmentId()))
                );
    }

    private static void validateAmountSufficiency(PlayerInventoryItem playerInventoryItem, PlayerEquipmentOperationCommand command) {
        if (playerInventoryItem.getAmount() < command.getAmount()) {
            throw new InvalidRequestException("Insufficient amount of equipment");
        }
    }

    private Specification<PlayerInventoryItem> getSpecification(UUID playerUuid, PlayerInventoryItemFilterParams filterParams) {
        List<Specification<PlayerInventoryItem>> specifications = new ArrayList<>();

        specifications.add(PlayerInventoryItemSpecifications.byPlayerUuid(playerUuid));

        if (!filterParams.getTypes().isEmpty()) {
            specifications.add(PlayerInventoryItemSpecifications.byEquipmentTypes(filterParams.getTypes()));
        }

        if (filterParams.getEnabled() != null) {
            specifications.add(PlayerInventoryItemSpecifications.byReferenceEquipmentEnabled(filterParams.getEnabled()));
        }

        return Specification.allOf(specifications);
    }
}
