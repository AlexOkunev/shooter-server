package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;


import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

public interface CurrencyGrpcClient {

    CurrencyInfo getCurrency(GetCurrencyRequest request);

    CurrencyInfo getEnabledCurrency(GetEnabledCurrencyRequest request);

    CurrencyInfoListPage getCurrencies(GetCurrenciesRequest request);

    CurrencyInfo createCurrency(CreateCurrencyRequest request);

    CurrencyInfo updateCurrency(UpdateCurrencyRequest request);
}
