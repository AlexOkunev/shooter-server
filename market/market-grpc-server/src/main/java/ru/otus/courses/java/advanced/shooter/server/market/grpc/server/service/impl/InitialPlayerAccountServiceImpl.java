package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.InitialPlayerAccountFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SavedInitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.UpdateInitialPlayerAccountCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.InitialPlayerAccountItemMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.InitialPlayerAccountItemRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.InitialPlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.InitialPlayerAccountItemSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util.TransactionExecutor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class InitialPlayerAccountServiceImpl implements InitialPlayerAccountService {
    private final InitialPlayerAccountItemRepository initialPlayerAccountItemRepository;
    private final InitialPlayerAccountItemMapper initialPlayerAccountItemMapper;
    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;
    private final TransactionExecutor transactionExecutor;

    @Override
    public Page<InitialPlayerAccountItem> getInitialPlayerAccountItems(
            @NotNull InitialPlayerAccountFilterParams filterParams,
            @NotNull Pageable pageable
    ) {
        Specification<InitialPlayerAccountItem> specification = getSpecification(filterParams);
        return initialPlayerAccountItemRepository.findAll(specification, pageable);
    }

    @Override
    public void saveInitialPlayerAccount(@Valid @NotNull UpdateInitialPlayerAccountCommand command) {
        validateItemCollections(command.getSavedItems(), command.getDeletedCurrencyIds());

        Map<Integer, SavedInitialPlayerAccountItem> savedItemsMap = command.getSavedItems().stream()
                .collect(Collectors.toMap(SavedInitialPlayerAccountItem::getCurrencyId, Function.identity()));

        Map<Integer, InitialPlayerAccountItem> foundItemsMap =
                initialPlayerAccountItemRepository.findAllById(savedItemsMap.keySet()).stream()
                        .collect(Collectors.toMap(InitialPlayerAccountItem::getCurrencyId, Function.identity()));

        List<InitialPlayerAccountItem> createdItems = savedItemsMap.entrySet().stream()
                .filter(entry -> !foundItemsMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(initialPlayerAccountItemMapper::toEntity)
                .toList();

        Collection<InitialPlayerAccountItem> updatedItems = foundItemsMap.values();

        updatedItems.forEach(item -> {
            SavedInitialPlayerAccountItem savedItem = savedItemsMap.get(item.getCurrencyId());
            checkVersionsEquality(item, savedItemsMap.get(item.getCurrencyId()));
            initialPlayerAccountItemMapper.update(item, savedItem);
        });

        transactionExecutor.execute(() -> {
            initialPlayerAccountItemRepository.saveAll(updatedItems);
            initialPlayerAccountItemRepository.saveAll(createdItems);
            initialPlayerAccountItemRepository.deleteAllById(command.getDeletedCurrencyIds());
        });
    }

    private void checkVersionsEquality(InitialPlayerAccountItem dbItem, SavedInitialPlayerAccountItem savedItem) {
        if (dbItem.getVersion() != savedItem.getVersion()) {
            throw new InvalidRequestException("Item (ID = %d) has incorrect version. Version in database is %d".formatted(
                    dbItem.getCurrencyId(), dbItem.getVersion()));
        }
    }

    private void validateItemCollections(Collection<SavedInitialPlayerAccountItem> savedItems, List<Integer> deletedCurrencyIds) {
        Set<Integer> uniqueSavedCurrencyIds = savedItems.stream()
                .map(SavedInitialPlayerAccountItem::getCurrencyId)
                .collect(Collectors.toSet());

        if (uniqueSavedCurrencyIds.size() != savedItems.size()) {
            throw new InvalidRequestException("Saved items contain duplicate currency IDs");
        }

        Collection<Integer> intersection = CollectionUtils.intersection(uniqueSavedCurrencyIds, deletedCurrencyIds);
        if (!intersection.isEmpty()) {
            throw new InvalidRequestException("Saved and deleted items contain duplicates: %s".formatted(
                    StringUtils.join(intersection)));
        }

        for (int currencyId : uniqueSavedCurrencyIds) {
            ReferenceCurrency currency = referenceCurrencyCacheService.getById(currencyId)
                    .orElseThrow(() -> new InvalidRequestException("Currency with ID %d not found".formatted(currencyId)));

            if (!currency.isEnabled()) {
                throw new InvalidRequestException("Currency with ID %d is not enabled".formatted(currencyId));
            }
        }
    }

    private Specification<InitialPlayerAccountItem> getSpecification(InitialPlayerAccountFilterParams filterParams) {
        List<Specification<InitialPlayerAccountItem>> specifications = new ArrayList<>();

        if (filterParams.getEnabled() != null) {
            specifications.add(InitialPlayerAccountItemSpecifications.byEnabled(filterParams.getEnabled()));
        }

        return Specification.allOf(specifications);
    }
}