package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

public interface CurrencyService {

    CurrencyInfo getCurrencyInfo(int currencyId);

    CurrencyInfo getEnabledCurrencyInfo(int currencyId);

    CurrencyInfo createCurrency(CreateCurrencyRequest request);

    CurrencyInfo updateCurrency(UpdateCurrencyRequest request);

    CurrencyInfoListPage getCurrencies(GetCurrenciesRequest request);
}
