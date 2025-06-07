package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectAlreadyExistsException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.*;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.EquipmentTypeMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.PlayerInventoryItemMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.PlayerInventoryLogEntryMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.InitialPlayerInventoryItemRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.PlayerInventoryItemRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.PlayerInventoryLogRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification.PlayerInventoryItemSpecifications;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.InitializePlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInventoryServiceImpl implements PlayerInventoryService {
    private final PlayerInventoryItemRepository playerInventoryItemRepository;

    private final PlayerInventoryLogRepository playerInventoryLogRepository;

    private final PlayerInventoryItemMapper playerInventoryItemMapper;

    private final PaginationInfoMapper paginationInfoMapper;

    private final EquipmentTypeMapper equipmentTypeMapper;

    private final InitialPlayerInventoryItemRepository initialPlayerInventoryItemRepository;

    private final PlayerInventoryLogEntryMapper playerInventoryLogEntryMapper;

    private final ReferenceEquipmentCacheService equipmentCacheService;

    private static final Sort DEFAULT_SORT = Sort.by(
            Sort.Order.asc(PlayerInventoryItem.Fields.equipmentType),
            Sort.Order.asc(PlayerInventoryItem.Fields.equipmentId)
    );

    @Override
    public PlayerInventoryItemsPage getPlayerInventoryItemsPage(GetPlayerInventoryRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Specification<PlayerInventoryItem> specification = getSpecification(request);

        Page<PlayerInventoryItem> data = playerInventoryItemRepository.findAll(specification, pageable);

        return PlayerInventoryItemsPage.newBuilder()
                .addAllData(data.map(playerInventoryItemMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    @Override
    @Transactional
    public void initializePlayerInventory(InitializePlayerInventoryRequest request) {
        playerInventoryItemRepository.findFirstByPlayerId(request.getPlayerId())
                .ifPresent(item -> {
                    throw new ObjectAlreadyExistsException("Player %d inventory is already initialized".formatted(request.getPlayerId()));
                });

        List<PlayerInventoryItem> inventoryItems = initialPlayerInventoryItemRepository.findAll().stream()
                .map(initialItem -> playerInventoryItemMapper.toEntity(initialItem, request.getPlayerId()))
                .toList();

        List<PlayerInventoryLogEntry> logEntries = inventoryItems.stream()
                .map(playerInventoryLogEntryMapper::toEntityInitialize)
                .toList();

        playerInventoryItemRepository.saveAll(inventoryItems);
        playerInventoryLogRepository.saveAll(logEntries);
    }

    @Override
    @Transactional
    public void giveEquipment(PlayerEquipmentOperationRequest request) {
        validateAmountPositivity(request);

        InventoryEquipmentType inventoryEquipmentType = equipmentTypeMapper.toEntity(request.getEquipmentType());
        ReferenceEquipment equipment = equipmentCacheService.getById(new ReferenceEquipmentId(request.getEquipmentId(), inventoryEquipmentType))
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(inventoryEquipmentType, request.getEquipmentId())));

        PlayerInventoryItemId playerInventoryItemId = new PlayerInventoryItemId(request.getPlayerId(), inventoryEquipmentType, equipment.getEquipmentId());
        PlayerInventoryItem playerInventoryItem = playerInventoryItemRepository.findById(playerInventoryItemId)
                .orElse(playerInventoryItemMapper.toEntityWithZeroAmount(request));

        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);

        playerInventoryItem.setAmount(playerInventoryItem.getAmount() + request.getAmount());
        playerInventoryItemRepository.save(playerInventoryItem);

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.ADMIN_GIVE);
        playerInventoryLogRepository.save(logEntry);
    }

    @Override
    @Transactional
    public void takeAwayEquipment(PlayerEquipmentOperationRequest request) {
        validateAmountPositivity(request);

        InventoryEquipmentType inventoryEquipmentType = equipmentTypeMapper.toEntity(request.getEquipmentType());
        ReferenceEquipment equipment = equipmentCacheService.getById(new ReferenceEquipmentId(request.getEquipmentId(), inventoryEquipmentType))
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(inventoryEquipmentType, request.getEquipmentId())));

        PlayerInventoryItem playerInventoryItem = getPlayerInventoryItem(request, inventoryEquipmentType, equipment);

        validateAmountSufficiency(request, playerInventoryItem);

        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);

        playerInventoryItem.setAmount(playerInventoryItem.getAmount() - request.getAmount());
        playerInventoryItemRepository.save(playerInventoryItem);

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.ADMIN_TAKE_AWAY);
        playerInventoryLogRepository.save(logEntry);
    }

    @Override
    @Transactional
    public void spendEquipment(PlayerEquipmentOperationRequest request) {
        validateAmountPositivity(request);

        InventoryEquipmentType inventoryEquipmentType = equipmentTypeMapper.toEntity(request.getEquipmentType());

        ReferenceEquipment equipment = equipmentCacheService.getById(new ReferenceEquipmentId(request.getEquipmentId(), inventoryEquipmentType))
                .orElseThrow(() -> new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(inventoryEquipmentType, request.getEquipmentId())));

        if (!equipment.isEnabled()) {
            log.error("Equipment {} (ID = {}) is not enabled", inventoryEquipmentType, request.getEquipmentId());
            throw new ObjectNotFoundException("Equipment %s with ID %d not found".formatted(inventoryEquipmentType, request.getEquipmentId()));
        }

        PlayerInventoryItem playerInventoryItem = getPlayerInventoryItem(request, inventoryEquipmentType, equipment);

        validateAmountSufficiency(request, playerInventoryItem);

        PlayerInventoryLogEntry logEntry = playerInventoryLogEntryMapper.toEntity(playerInventoryItem);

        playerInventoryItem.setAmount(playerInventoryItem.getAmount() - request.getAmount());
        playerInventoryItemRepository.save(playerInventoryItem);

        playerInventoryLogEntryMapper.update(logEntry, playerInventoryItem, OperationType.SPEND);
        playerInventoryLogRepository.save(logEntry);
    }

    private PlayerInventoryItem getPlayerInventoryItem(PlayerEquipmentOperationRequest request, InventoryEquipmentType inventoryEquipmentType, ReferenceEquipment equipment) {
        PlayerInventoryItemId playerInventoryItemId = new PlayerInventoryItemId(request.getPlayerId(), inventoryEquipmentType, equipment.getEquipmentId());

        return playerInventoryItemRepository.findById(playerInventoryItemId)
                .orElseThrow(() -> new ObjectNotFoundException("Player %d inventory item (%s with id %s) not found".formatted(
                        playerInventoryItemId.getPlayerId(), playerInventoryItemId.getEquipmentType().name(), playerInventoryItemId.getEquipmentId())));
    }

    private static void validateAmountSufficiency(PlayerEquipmentOperationRequest request, PlayerInventoryItem playerInventoryItem) {
        if (playerInventoryItem.getAmount() < request.getAmount()) {
            throw new InvalidRequestException("Insufficient amount of equipment");
        }
    }

    private static void validateAmountPositivity(PlayerEquipmentOperationRequest request) {
        if (request.getAmount() <= 0) {
            throw new InvalidRequestException("Amount must be greater than zero");
        }
    }

    private Specification<PlayerInventoryItem> getSpecification(GetPlayerInventoryRequest request) {
        List<Specification<PlayerInventoryItem>> specifications = new ArrayList<>();

        specifications.add(PlayerInventoryItemSpecifications.byPlayerId(request.getPlayerId()));

        if (request.getTypesCount() > 0) {
            List<Integer> equipmentTypeCodes = request.getTypesList().stream()
                    .map(equipmentTypeMapper::toEntity)
                    .map(InventoryEquipmentType::getCode)
                    .toList();

            specifications.add(PlayerInventoryItemSpecifications.byEquipmentTypeCodes(equipmentTypeCodes));
        }

        return Specification.allOf(specifications);
    }
}
