package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.InitialPlayerInventoryFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.SavedInitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.UpdateInitialPlayerInventoryCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache.base.ReferenceEquipmentCacheService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain.InitialPlayerInventoryItemMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.InitialPlayerInventoryItemRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.InitialPlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification.InitialPlayerInventorySpecifications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class InitialPlayerInventoryServiceImpl implements InitialPlayerInventoryService {
    private final InitialPlayerInventoryItemMapper initialPlayerInventoryItemMapper;
    private final InitialPlayerInventoryItemRepository initialPlayerInventoryItemRepository;
    private final ReferenceEquipmentCacheService equipmentCacheService;

    @Override
    public Page<InitialPlayerInventoryItem> getInitialPlayerInventory(
            @NotNull InitialPlayerInventoryFilterParams filterParams,
            @NotNull Pageable pageable
    ) {
        Specification<InitialPlayerInventoryItem> specification = getSpecification(filterParams);
        return initialPlayerInventoryItemRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void updateInitialPlayerInventory(@Valid @NotNull UpdateInitialPlayerInventoryCommand command) {
        Map<ReferenceEquipmentId, SavedInitialPlayerInventoryItem> savedItems = command.getSavedItems().stream()
                .collect(Collectors.toMap(initialPlayerInventoryItemMapper::toEntityId, Function.identity()));

        List<ReferenceEquipmentId> deletedItemIds = command.getDeletedItems().stream()
                .map(initialPlayerInventoryItemMapper::toEntityId)
                .toList();

        validateItemCollections(savedItems.keySet(), deletedItemIds);

        Map<ReferenceEquipmentId, InitialPlayerInventoryItem> foundItemsMap =
                initialPlayerInventoryItemRepository.findAllById(savedItems.keySet()).stream()
                        .collect(Collectors.toMap(InitialPlayerInventoryItem::getId, Function.identity()));

        List<InitialPlayerInventoryItem> createdItems = savedItems.entrySet().stream()
                .filter(entry -> !foundItemsMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(initialPlayerInventoryItemMapper::toEntity)
                .toList();

        Collection<InitialPlayerInventoryItem> updatedItems = foundItemsMap.values();

        updatedItems.forEach(item -> {
            SavedInitialPlayerInventoryItem savedItem = savedItems.get(item.getId());
            checkVersionsEquality(item, savedItem);
            initialPlayerInventoryItemMapper.update(item, savedItem);
        });

        initialPlayerInventoryItemRepository.saveAll(updatedItems);
        initialPlayerInventoryItemRepository.saveAll(createdItems);
        initialPlayerInventoryItemRepository.deleteAllById(deletedItemIds);
    }

    private void validateItemCollections(Collection<ReferenceEquipmentId> savedItemIds, Collection<ReferenceEquipmentId> deletedItemIds) {
        Collection<ReferenceEquipmentId> intersection = CollectionUtils.intersection(savedItemIds, deletedItemIds);
        if (!intersection.isEmpty()) {
            String intersectionString = intersection.stream()
                    .map(id -> "%s (ID = %d)".formatted(id.getEquipmentType().name(), id.getEquipmentId()))
                    .collect(Collectors.joining(", "));
            throw new InvalidRequestException("Saved and deleted items contain duplicates: %s".formatted(intersectionString));
        }

        savedItemIds.stream()
                .filter(id -> equipmentCacheService.getById(id).isEmpty())
                .findFirst()
                .ifPresent(item -> {
                    throw new InvalidRequestException("Saved item %s (ID = %d) not found".formatted(
                            item.getEquipmentType().name(),
                            item.getEquipmentId())
                    );
                });

        deletedItemIds.stream()
                .filter(id -> equipmentCacheService.getById(id).isEmpty())
                .findFirst()
                .ifPresent(item -> {
                    throw new InvalidRequestException("Deleted item %s (ID = %d) not found".formatted(
                            item.getEquipmentType().name(),
                            item.getEquipmentId())
                    );
                });
    }

    private void checkVersionsEquality(InitialPlayerInventoryItem dbItem, SavedInitialPlayerInventoryItem savedItem) {
        if (dbItem.getVersion() != savedItem.getVersion()) {
            throw new InvalidRequestException("Item %s (ID = %d) has incorrect version. Version in database is %d".formatted(
                    dbItem.getId().getEquipmentType().name(), dbItem.getId().getEquipmentId(), dbItem.getVersion()));
        }
    }

    private Specification<InitialPlayerInventoryItem> getSpecification(InitialPlayerInventoryFilterParams filter) {
        List<Specification<InitialPlayerInventoryItem>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(InitialPlayerInventorySpecifications.byEnabled(filter.getEnabled()));
        }

        return Specification.allOf(specifications);
    }
}
