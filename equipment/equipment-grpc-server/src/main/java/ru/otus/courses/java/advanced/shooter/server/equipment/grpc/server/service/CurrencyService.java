package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencyFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencySavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;

public interface CurrencyService {

    Currency getCurrency(int currencyId);

    Currency getEnabledCurrency(int currencyId);

    Currency createCurrency(@NotNull @Valid CurrencySavedData currencySavedData);

    Currency updateCurrency(int currencyId, @NotNull @Valid CurrencySavedData currencySavedData);

    Page<Currency> getCurrencies(@NotNull CurrencyFilterParams filterParams, @NotNull Pageable pageable);
}
