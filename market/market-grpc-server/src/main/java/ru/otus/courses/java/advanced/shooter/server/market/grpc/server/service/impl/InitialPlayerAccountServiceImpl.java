package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.InitialPlayerAccountItemMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.InitialPlayerAccountItemRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.InitialPlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.InitialPlayerAccountItemSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util.TransactionExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.ModifyInitialPlayerAccountRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialPlayerAccountServiceImpl implements InitialPlayerAccountService {
    private final InitialPlayerAccountItemRepository initialPlayerAccountItemRepository;

    private final InitialPlayerAccountItemMapper initialPlayerAccountItemMapper;

    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;

    private final PaginationInfoMapper paginationInfoMapper;

    private final TransactionExecutor transactionExecutor;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Order.asc(InitialPlayerAccountItem.Fields.currencyId));

    @Override
    public InitialPlayerAccountItemsPage getInitialPlayerInventory(GetInitialPlayerAccountRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<InitialPlayerAccountItem> specification = getSpecification(request);

        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<InitialPlayerAccountItem> data = initialPlayerAccountItemRepository.findAll(specification, pageable);

        return InitialPlayerAccountItemsPage.newBuilder()
                .addAllData(data.map(initialPlayerAccountItemMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    @Override
    public void modifyInitialPlayerAccount(ModifyInitialPlayerAccountRequest request) {
        if (request.getItemsCount() == 0) {
            return;
        }

        validateModifyInitialPlayerAccountRequest(request);

        Map<Integer, InitialPlayerAccountItemRequest> requestMap = request.getItemsList().stream()
                .collect(Collectors.toMap(InitialPlayerAccountItemRequest::getCurrencyId, Function.identity()));

        Map<Integer, InitialPlayerAccountItem> foundItemsMap = initialPlayerAccountItemRepository.findAllById(requestMap.keySet())
                .stream()
                .collect(Collectors.toMap(InitialPlayerAccountItem::getCurrencyId, Function.identity()));

        List<InitialPlayerAccountItem> createdItems = requestMap.entrySet().stream()
                .filter(entry -> !foundItemsMap.containsKey(entry.getKey()))
                .map(Map.Entry::getValue)
                .map(initialPlayerAccountItemMapper::toEntity)
                .toList();

        List<InitialPlayerAccountItem> updatedItems = foundItemsMap.entrySet().stream()
                .map(entry -> Pair.of(entry.getValue(), requestMap.get(entry.getKey())))
                .peek(pair -> checkVersionsEquality(pair.getLeft(), pair.getRight()))
                .map(pair -> {
                    pair.getLeft().setAmount(pair.getRight().getAmount());
                    pair.getLeft().setEnabled(pair.getRight().getEnabled());
                    return pair.getLeft();
                })
                .toList();

        transactionExecutor.execute(() -> {
            initialPlayerAccountItemRepository.saveAll(updatedItems);
            initialPlayerAccountItemRepository.saveAll(createdItems);
        });
    }

    private void checkVersionsEquality(InitialPlayerAccountItem dbItem, InitialPlayerAccountItemRequest request) {
        if (dbItem.getVersion() != request.getVersion()) {
            throw new InvalidRequestException("Item (ID = %d) has incorrect version. Version in database is %d".formatted(
                    dbItem.getCurrencyId(), dbItem.getVersion()));
        }
    }

    private void validateModifyInitialPlayerAccountRequest(ModifyInitialPlayerAccountRequest request) {
        Set<Integer> uniqueCurrencyIds = request.getItemsList().stream()
                .map(InitialPlayerAccountItemRequest::getCurrencyId)
                .collect(Collectors.toSet());

        if (uniqueCurrencyIds.size() != request.getItemsCount()) {
            throw new InvalidRequestException("Currency IDs in request must be unique");
        }

        request.getItemsList().stream()
                .filter(item -> item.getAmount() <= 0)
                .findFirst()
                .ifPresent(item -> {
                    throw new InvalidRequestException("Item (ID = %d) must have positive amount".formatted(item.getCurrencyId()));
                });

        for (var item : request.getItemsList()) {
            ReferenceCurrency currency = referenceCurrencyCacheService.getById(item.getCurrencyId())
                    .orElseThrow(() -> new InvalidRequestException("Currency with ID %d not found".formatted(item.getCurrencyId())));

            if (!currency.isEnabled()) {
                throw new InvalidRequestException("Currency with ID %d is not enabled".formatted(item.getCurrencyId()));
            }
        }
    }

    private Specification<InitialPlayerAccountItem> getSpecification(GetInitialPlayerAccountRequest request) {
        List<Specification<InitialPlayerAccountItem>> specifications = new ArrayList<>();

        if (request.hasFilter()) {
            if (request.getFilter().hasEnabled()) {
                specifications.add(InitialPlayerAccountItemSpecifications.byEnabled(request.getFilter().getEnabled()));
            }
        }

        return Specification.allOf(specifications);
    }
}