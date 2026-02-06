package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

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
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.cache.base.ReferenceCurrencyCacheService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain.MoneyBundleMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.MoneyBundleRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification.MoneyBundleSpecifications;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class MoneyBundleServiceImpl implements MoneyBundleService {

    private final MoneyBundleRepository moneyBundleRepository;
    private final ReferenceCurrencyCacheService referenceCurrencyCacheService;
    private final MoneyBundleMapper moneyBundleMapper;

    @Override
    public MoneyBundle getMoneyBundle(int id) {
        return moneyBundleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id '%d' not found".formatted(id)));
    }

    @Override
    public MoneyBundle getEnabledMoneyBundle(int id) {
        return moneyBundleRepository.findByIdAndEnabledIsTrue(id)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id '%d' not found".formatted(id)));
    }

    @Override
    @Transactional
    public MoneyBundle createMoneyBundle(@Valid @NotNull MoneyBundleSavedData data) {
        int currencyId = data.getCurrencyId();

        ReferenceCurrency currency = referenceCurrencyCacheService.getById(currencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(currencyId)));

        validateCurrencyForMoneyBundle(currency);

        MoneyBundle moneyBundle = moneyBundleMapper.toEntity(data);

        return moneyBundleRepository.save(moneyBundle);
    }

    @Override
    @Transactional
    public MoneyBundle updateMoneyBundle(
            int id,
            int version,
            @Valid @NotNull MoneyBundleSavedData data
    ) {
        MoneyBundle moneyBundle = moneyBundleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Money bundle with id '%d' not found".formatted(id)));

        if (moneyBundle.getVersion() != version) {
            throw new InvalidRequestException("Money bundle version (%d) is not equal to version in request".formatted(version));
        }

        int currencyId = data.getCurrencyId();
        ReferenceCurrency currency = referenceCurrencyCacheService.getById(currencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id %d not found".formatted(currencyId)));

        validateCurrencyForMoneyBundle(currency);

        moneyBundleMapper.update(moneyBundle, data);

        return moneyBundleRepository.save(moneyBundle);
    }

    @Override
    public Page<MoneyBundle> getMoneyBundles(
            @NotNull MoneyBundleFilterParams filterParams,
            @NotNull Pageable pageable
    ) {
        Specification<MoneyBundle> specification = getSpecification(filterParams);
        return moneyBundleRepository.findAll(specification, pageable);
    }

    private void validateCurrencyForMoneyBundle(ReferenceCurrency referenceCurrency) {
        if (!referenceCurrency.isEnabled()) {
            throw new InvalidRequestException("Currency is disabled");
        }

        if (!referenceCurrency.isCanBeBought()) {
            throw new InvalidRequestException("Currency can not be traded");
        }
    }

    private Specification<MoneyBundle> getSpecification(MoneyBundleFilterParams filter) {
        List<Specification<MoneyBundle>> specifications = new ArrayList<>();

        if (filter.getEnabled() != null) {
            specifications.add(MoneyBundleSpecifications.byEnabled(filter.getEnabled()));
        }

        if (filter.getCurrencyEnabled() != null) {
            specifications.add(MoneyBundleSpecifications.byCurrencyEnabled(filter.getCurrencyEnabled()));
        }

        if (filter.getCurrencyCanBeBought() != null) {
            specifications.add(MoneyBundleSpecifications.byCurrencyCanBeBought(filter.getCurrencyCanBeBought()));
        }

        if (!filter.getCurrencyIds().isEmpty()) {
            specifications.add(MoneyBundleSpecifications.byCurrencyIds(filter.getCurrencyIds()));
        }

        return Specification.allOf(specifications);
    }
}