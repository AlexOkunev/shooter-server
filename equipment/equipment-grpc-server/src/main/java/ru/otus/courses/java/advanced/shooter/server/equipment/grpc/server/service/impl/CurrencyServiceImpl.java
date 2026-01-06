package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.impl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencyFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencySavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain.CurrencyMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.repository.CurrencyRepository;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.CurrencyService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications.CurrencySpecifications;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated //TODO!!! check validation
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @Override
    public Currency getCurrency(int currencyId) {
        return currencyRepository.findById(currencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id '%d' not found".formatted(currencyId)));
    }

    @Override
    public Currency getEnabledCurrency(int currencyId) {
        return currencyRepository.findByIdAndEnabled(currencyId, true)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id '%d' not found".formatted(currencyId)));
    }

    @Override
    public Currency createCurrency(@Valid @NotNull CurrencySavedData currencySavedData) {
        Currency currency = currencyMapper.toEntity(currencySavedData);
        return currencyRepository.save(currency);
    }

    @Override
    public Currency updateCurrency(int currencyId, @NotNull @Valid CurrencySavedData currencySavedData) {
        Currency currency = currencyRepository.findById(currencyId)
                .orElseThrow(() -> new ObjectNotFoundException("Currency with id '%d' not found".formatted(currencyId)));

        currencyMapper.updateCurrency(currency, currencySavedData);
        return currencyRepository.save(currency);
    }

    @Override
    public Page<Currency> getCurrencies(@NotNull CurrencyFilterParams filterParams, @NotNull Pageable pageable) {
        Specification<Currency> specification = getSpecification(filterParams);
        return currencyRepository.findAll(specification, pageable);
    }

    private static Specification<Currency> getSpecification(CurrencyFilterParams filterParams) {
        List<Specification<Currency>> specifications = new ArrayList<>();

        if (filterParams.getEnabled() != null) {
            specifications.add(CurrencySpecifications.byEnabled(filterParams.getEnabled()));
        }

        if (filterParams.getName() != null) {
            specifications.add(CurrencySpecifications.byNameStartsWith(filterParams.getName()));
        }

        if (filterParams.getCanBeBought() != null) {
            specifications.add(CurrencySpecifications.byCanBeBought(filterParams.getCanBeBought()));
        }

        if (filterParams.getCanBeGivenAsAward() != null) {
            specifications.add(CurrencySpecifications.byCanBeGivenAsAward(filterParams.getCanBeGivenAsAward()));
        }

        if (!filterParams.getCurrencyIds().isEmpty()) {
            specifications.add(CurrencySpecifications.byCurrencyIds(filterParams.getCurrencyIds()));
        }

        if (filterParams.getUpdatedAfter() != null) {
            specifications.add(CurrencySpecifications.byUpdatedAfter(filterParams.getUpdatedAfter()));
        }

        return Specification.allOf(specifications);
    }
}
