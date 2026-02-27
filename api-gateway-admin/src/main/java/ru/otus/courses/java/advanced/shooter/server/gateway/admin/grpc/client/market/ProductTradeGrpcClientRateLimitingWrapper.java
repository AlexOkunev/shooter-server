package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.*;

import java.util.List;

@Slf4j
@Component(ProductTradeGrpcClientRateLimitingWrapper.NAME)
public class ProductTradeGrpcClientRateLimitingWrapper implements ProductTradeGrpcClient {

    public static final String NAME = "productTradeGrpcClientRateLimitingWrapper";

    private final ProductTradeGrpcClient productTradeGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;


    public ProductTradeGrpcClientRateLimitingWrapper(
            @Qualifier(ProductTradeGrpcClientBaseImpl.NAME) ProductTradeGrpcClient productTradeGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.productTradeGrpcClient = productTradeGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public ProductTradeInfo createProductTrade(CreateProductTradeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productTradeGrpcClient.createProductTrade(request),
                rateLimiters
        );
    }

    @Override
    public ProductTradeInfo getProductTrade(GetProductTradeRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productTradeGrpcClient.getProductTrade(request),
                rateLimiters
        );
    }

    @Override
    public ProductTradeInfoListPage getProductTrades(GetProductTradesRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productTradeGrpcClient.getProductTrades(request),
                rateLimiters
        );
    }
}
