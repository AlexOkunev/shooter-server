package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.*;

import java.util.List;

@Slf4j
@Component(MoneyBundleTradeGrpcClientRateLimitingWrapper.NAME)
public class MoneyBundleTradeGrpcClientRateLimitingWrapper implements MoneyBundleTradeGrpcClient {

    public static final String NAME = "moneyBundleTradeGrpcClientRateLimitingWrapper";

    private final MoneyBundleTradeGrpcClient moneyBundleTradeGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public MoneyBundleTradeGrpcClientRateLimitingWrapper(
            @Qualifier(MoneyBundleTradeGrpcClientBaseImpl.NAME) MoneyBundleTradeGrpcClient moneyBundleTradeGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.moneyBundleTradeGrpcClient = moneyBundleTradeGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public MoneyBundleTradeInfo createMoneyBundleTrade(CreateMoneyBundleTradeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleTradeGrpcClient.createMoneyBundleTrade(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleTradeInfo performMoneyBundleTradePayment(PerformMoneyBundleTradePaymentRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleTradeGrpcClient.performMoneyBundleTradePayment(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleTradeInfo getMoneyBundleTrade(GetMoneyBundleTradeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleTradeGrpcClient.getMoneyBundleTrade(request),
                rateLimiters
        );
    }

    @Override
    public MoneyBundleTradeInfoListPage getMoneyBundleTrades(GetMoneyBundleTradesRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> moneyBundleTradeGrpcClient.getMoneyBundleTrades(request),
                rateLimiters
        );
    }
}
