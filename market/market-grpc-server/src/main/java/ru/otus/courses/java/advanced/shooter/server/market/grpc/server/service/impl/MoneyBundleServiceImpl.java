package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.InvalidRequestException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.common.utils.validation.ValidationUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.event.MoneyBundleChangedEvent;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.MoneyBundleMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.MoneyBundleSpecifications;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.util.TransactionExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyBundleServiceImpl implements MoneyBundleService {
    private final MoneyBundleRepository moneyBundleRepository;

    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;

    private final MoneyBundleMapper moneyBundleMapper;

    private final PaginationInfoMapper paginationInfoMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TransactionExecutor transactionExecutor;

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, MoneyBundle.Fields.id);

    @Override
    public MoneyBundleInfo getMoneyBundle(GetMoneyBundleRequest request) {
        return moneyBundleRepository.findById(request.getId())
                .map(moneyBundleMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id '%d' not found".formatted(request.getId())));
    }

    @Override
    public MoneyBundleInfo getEnabledMoneyBundle(GetMoneyBundleRequest request) {
        return moneyBundleRepository.findByIdAndEnabledIsTrue(request.getId())
                .map(moneyBundleMapper::toResponse)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id '%d' not found".formatted(request.getId())));
    }

    @Override
    public MoneyBundleInfoListPage getMoneyBundles(GetMoneyBundlesRequest request) {
        if (request.hasPaginationRequest()) {
            ValidationUtils.validatePaginationRequest(request.getPaginationRequest());
        }

        Specification<MoneyBundle> specification = getSpecification(request.getFilter());
        Pageable pageable = request.hasPaginationRequest() ?
                PageRequest.of(request.getPaginationRequest().getPage(), request.getPaginationRequest().getCount(), DEFAULT_SORT) :
                PageRequest.of(0, 10, DEFAULT_SORT);

        Page<MoneyBundle> data = moneyBundleRepository.findAll(specification, pageable);

        return MoneyBundleInfoListPage.newBuilder()
                .addAllData(data.map(moneyBundleMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();
    }

    @Override
    public MoneyBundleInfo createMoneyBundle(CreateMoneyBundleRequest request) {
        if (!request.hasData()) {
            throw new InvalidRequestException("Data must not be empty");
        }

        validateMoneyBundleWritableData(request.getData());

        int currencyId = request.getData().getCurrencyId();
        ReferenceCurrency currency = referenceCurrencyCacheService.getById(currencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(currencyId)));

        validateCurrencyForMoneyBundle(currency);

        MoneyBundle moneyBundle = moneyBundleMapper.toEntity(request.getData(), currency);
        moneyBundle = moneyBundleRepository.save(moneyBundle);

        return moneyBundleMapper.toResponse(moneyBundle);
    }

    @Override
    public MoneyBundleInfo updateMoneyBundle(UpdateMoneyBundleRequest request) {
        MoneyBundle moneyBundle = moneyBundleRepository.findById(request.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id '%d' not found".formatted(request.getId())));

        if (moneyBundle.getVersion() != request.getVersion()) {
            throw new InvalidRequestException("Money bundle version does not match money bundle version in request");
        }

        if (!request.hasData()) {
            return moneyBundleMapper.toResponse(moneyBundle);
        }

        validateMoneyBundleWritableData(request.getData());

        int currencyId = request.getData().getCurrencyId();
        ReferenceCurrency currency = referenceCurrencyCacheService.getById(currencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(currencyId)));

        validateCurrencyForMoneyBundle(currency);

        moneyBundleMapper.update(moneyBundle, request.getData(), currency);

        MoneyBundle savedMoneyBundle = transactionExecutor.execute(() -> {
            MoneyBundle temp = moneyBundleRepository.save(moneyBundle);
            applicationEventPublisher.publishEvent(new MoneyBundleChangedEvent(moneyBundle.getId()));
            return temp;
        });

        return moneyBundleMapper.toResponse(savedMoneyBundle);
    }

    private void validateCurrencyForMoneyBundle(ReferenceCurrency referenceCurrency) {
        if (!referenceCurrency.isEnabled()) {
            throw new InvalidRequestException("Currency is disabled");
        }

        if (!referenceCurrency.isCanBeTraded()) {
            throw new InvalidRequestException("Currency can not be traded");
        }
    }

    private void validateMoneyBundleWritableData(MoneyBundleWritableData data) {
        if (data.getCurrencyAmount() <= 0) {
            throw new InvalidRequestException("Currency amount must be greater than 0");
        }

        if (data.getRublesPrice() <= 0) {
            throw new InvalidRequestException("Rubles price must be greater than 0");
        }
    }

    private Specification<MoneyBundle> getSpecification(GetMoneyBundlesRequest.Filter filter) {
        List<Specification<MoneyBundle>> specifications = new ArrayList<>();

        if (filter.getIdsCount() > 0) {
            specifications.add(MoneyBundleSpecifications.byIds(filter.getIdsList()));
        }

        if (filter.hasEnabled()) {
            specifications.add(MoneyBundleSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.hasCurrencyEnabled()) {
            specifications.add(MoneyBundleSpecifications.byCurrencyEnabled(filter.getCurrencyEnabled()));
        }

        if (filter.hasCurrencyCanBeTraded()) {
            specifications.add(MoneyBundleSpecifications.byCurrencyCanBeTraded(filter.getCurrencyCanBeTraded()));
        }

        if (filter.getCurrencyIdsCount() > 0) {
            specifications.add(MoneyBundleSpecifications.byCurrencyIds(filter.getCurrencyIdsList()));
        }

        return Specification.allOf(specifications);
    }
}