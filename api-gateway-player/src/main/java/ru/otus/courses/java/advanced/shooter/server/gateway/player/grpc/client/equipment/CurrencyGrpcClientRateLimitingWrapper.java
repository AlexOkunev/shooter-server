package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.equipment.CurrencyGrpcClientBaseImpl;

import java.util.List;

@Slf4j
@Component(CurrencyGrpcClientRateLimitingWrapper.NAME)
public class CurrencyGrpcClientRateLimitingWrapper implements CurrencyGrpcClient {

    public static final String NAME = "currencyGrpcClientRateLimitingWrapper";

    private final CurrencyGrpcClient currencyGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public CurrencyGrpcClientRateLimitingWrapper(
            @Qualifier(CurrencyGrpcClientBaseImpl.NAME) CurrencyGrpcClient currencyGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.currencyGrpcClient = currencyGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_equipment_rps"),
                rateLimiterRegistry.rateLimiter("grpc_equipment_rpm")
        );
    }

    @Override
    public CurrencyInfo getCurrency(GetCurrencyRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> currencyGrpcClient.getCurrency(request),
                rateLimiters
        );
    }

    @Override
    public CurrencyInfo getEnabledCurrency(GetEnabledCurrencyRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> currencyGrpcClient.getEnabledCurrency(request),
                rateLimiters
        );
    }

    @Override
    public CurrencyInfoListPage getCurrencies(GetCurrenciesRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> currencyGrpcClient.getCurrencies(request),
                rateLimiters
        );
    }

    @Override
    public CurrencyInfo createCurrency(CreateCurrencyRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> currencyGrpcClient.createCurrency(request),
                rateLimiters
        );
    }

    @Override
    public CurrencyInfo updateCurrency(UpdateCurrencyRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> currencyGrpcClient.updateCurrency(request),
                rateLimiters
        );
    }
}
