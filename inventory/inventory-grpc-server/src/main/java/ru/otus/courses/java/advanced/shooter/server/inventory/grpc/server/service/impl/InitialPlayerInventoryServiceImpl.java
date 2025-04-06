package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItemId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.EquipmentTypeMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.InitialPlayerInventoryItemMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.InitialPlayerInventoryItemRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.EquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.InitialPlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification.InitialPlayerInventorySpecifications;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.util.PaginationUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.util.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.ModifyInitialPlayerInventoryRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialPlayerInventoryServiceImpl implements InitialPlayerInventoryService {
    private final InitialPlayerInventoryItemMapper initialPlayerInventoryItemMapper;

    private final EquipmentTypeMapper equipmentTypeMapper;

    private final InitialPlayerInventoryItemRepository initialPlayerInventoryItemRepository;

    private final EquipmentCacheService equipmentCacheService;

    private final Sort defaultSort = Sort.by(
            Sort.Order.asc(InitialPlayerInventoryItem.Fields.equipmentType),
            Sort.Order.asc(InitialPlayerInventoryItem.Fields.equipmentId)
    );

    @Override
    public InitialPlayerInventoryItemsPage getInitialPlayerInventory(GetInitialPlayerInventoryRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<InitialPlayerInventoryItem> specification = getSpecification(request);

        Pageable pageable = request.hasPaginationRequest() ?
                PaginationUtils.getPageable(request.getPaginationRequest(), defaultSort) :
                PaginationUtils.getPageable(0, 10, defaultSort);

        Page<InitialPlayerInventoryItem> data = initialPlayerInventoryItemRepository.findAll(specification, pageable);

        return InitialPlayerInventoryItemsPage.newBuilder()
                .addAllData(data.map(initialPlayerInventoryItemMapper::toResponse))
                .setTotalCount(data.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public void modifyInitialPlayerInventory(ModifyInitialPlayerInventoryRequest request) {
        if (request.getItemsCount() == 0) {
            return;
        }

        validateModifyInitialPlayerInventoryRequest(request);

        Map<InitialPlayerInventoryItemId, InitialPlayerInventoryItemRequest> requestMap = request.getItemsList().stream()
                .collect(Collectors.toMap(item -> new InitialPlayerInventoryItemId(
                                equipmentTypeMapper.toEntity(item.getEquipmentType()), item.getEquipmentId()),
                        Function.identity()));

        Map<InitialPlayerInventoryItemId, InitialPlayerInventoryItem> foundItemsMap =
                initialPlayerInventoryItemRepository.findAllById(requestMap.keySet()).stream()
                        .collect(Collectors.toMap(
                                item -> new InitialPlayerInventoryItemId(item.getEquipmentType(), item.getEquipmentId()),
                                Function.identity())
                        );

        List<InitialPlayerInventoryItem> createdItems = requestMap.entrySet().stream()
                .filter(entry -> !foundItemsMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(initialPlayerInventoryItemMapper::toEntity)
                .toList();

        List<InitialPlayerInventoryItem> updatedItems = foundItemsMap.entrySet().stream()
                .map(entry -> Pair.of(entry.getValue(), requestMap.get(entry.getKey())))
                .peek(pair -> checkVersionsEquality(pair.getLeft(), pair.getRight()))
                .map(pair -> {
                    pair.getLeft().setAmount(pair.getRight().getAmount());
                    pair.getLeft().setEnabled(pair.getRight().getEnabled());
                    return pair.getLeft();
                })
                .toList();

        initialPlayerInventoryItemRepository.saveAll(updatedItems);
        initialPlayerInventoryItemRepository.saveAll(createdItems);
    }

    private void checkVersionsEquality(InitialPlayerInventoryItem dbItem, InitialPlayerInventoryItemRequest request) {
        if (dbItem.getVersion() != request.getVersion()) {
            throw new InvalidRequestException("Item %s (ID = %d) has incorrect version. Version in database is %d".formatted(
                    dbItem.getEquipmentType().name(), dbItem.getEquipmentId(), dbItem.getVersion()));
        }
    }

    private void validateModifyInitialPlayerInventoryRequest(ModifyInitialPlayerInventoryRequest request) {
        request.getItemsList().stream()
                .filter(item -> item.getAmount() <= 0)
                .findFirst()
                .ifPresent(item -> {
                    throw new InvalidRequestException("Item %s (ID = %d) must have positive amount".formatted(item.getEquipmentType().name(), item.getEquipmentId()));
                });

        request.getItemsList().stream()
                .filter(item ->
                        equipmentCacheService.getOptionalFromCache(
                                        equipmentTypeMapper.toEntity(item.getEquipmentType()),
                                        item.getEquipmentId())
                                .isEmpty())
                .findFirst()
                .ifPresent(item -> {
                    throw new InvalidRequestException("Item %s (ID = %d) not found".formatted(item.getEquipmentType().name(), item.getEquipmentId()));
                });
    }

    private Specification<InitialPlayerInventoryItem> getSpecification(GetInitialPlayerInventoryRequest request) {
        if (!request.hasFilter()) {
            return Specification.allOf();
        }

        return Specification.allOf(InitialPlayerInventorySpecifications.byEnabled(request.getFilter().getEnabled()));
    }
}
