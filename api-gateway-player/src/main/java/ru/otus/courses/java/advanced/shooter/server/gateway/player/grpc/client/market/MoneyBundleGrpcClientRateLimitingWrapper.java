package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

import java.util.List;

@Slf4j
@Component(MoneyBundleGrpcClientRateLimitingWrapper.NAME)
public class MoneyBundleGrpcClientRateLimitingWrapper implements MoneyBundleGrpcClient {

    public static final String NAME = "moneyBundleGrpcClientRateLimitingWrapper";

    private final MoneyBundleGrpcClient moneyBundleGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public MoneyBundleGrpcClientRateLimitingWrapper(
            @Qualifier(MoneyBundleGrpcClientBaseImpl.NAME) MoneyBundleGrpcClient moneyBundleGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.moneyBundleGrpcClient = moneyBundleGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public MoneyBundleInfo getMoneyBundle(GetMoneyBundleRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleGrpcClient.getMoneyBundle(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleInfo getEnabledMoneyBundle(GetMoneyBundleRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleGrpcClient.getEnabledMoneyBundle(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleInfoListPage getMoneyBundles(GetMoneyBundlesRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleGrpcClient.getMoneyBundles(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleInfo createMoneyBundle(CreateMoneyBundleRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleGrpcClient.createMoneyBundle(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleInfo updateMoneyBundle(UpdateMoneyBundleRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleGrpcClient.updateMoneyBundle(request),
                rateLimiters
        );
    }
}
