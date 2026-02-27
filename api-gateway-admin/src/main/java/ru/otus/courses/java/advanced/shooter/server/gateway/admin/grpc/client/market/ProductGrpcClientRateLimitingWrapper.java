package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.RateLimitingGrpcCallExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

import java.util.List;

@Slf4j
@Component(ProductGrpcClientRateLimitingWrapper.NAME)
public class ProductGrpcClientRateLimitingWrapper implements ProductGrpcClient {

    public static final String NAME = "productGrpcClientRateLimitingWrapper";

    private final ProductGrpcClient productGrpcClient;
    private final RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor;
    private final List<RateLimiter> rateLimiters;

    public ProductGrpcClientRateLimitingWrapper(
            @Qualifier(ProductGrpcClientBaseImpl.NAME) ProductGrpcClient productGrpcClient,
            RateLimitingGrpcCallExecutor rateLimitingGrpcCallExecutor,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        this.productGrpcClient = productGrpcClient;
        this.rateLimitingGrpcCallExecutor = rateLimitingGrpcCallExecutor;
        this.rateLimiters = List.of(
                rateLimiterRegistry.rateLimiter("grpc_market_rps"),
                rateLimiterRegistry.rateLimiter("grpc_market_rpm")
        );
    }

    @Override
    public ProductInfo getProduct(GetProductRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productGrpcClient.getProduct(request),
                rateLimiters
        );
    }

    @Override
    public ProductInfo getEnabledProduct(GetProductRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productGrpcClient.getEnabledProduct(request),
                rateLimiters
        );
    }

    @Override
    public ProductInfoListPage getProducts(GetProductsRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productGrpcClient.getProducts(request),
                rateLimiters
        );
    }

    @Override
    public ProductInfo createProduct(CreateProductRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productGrpcClient.createProduct(request),
                rateLimiters
        );
    }

    @Override
    public ProductInfo updateProduct(UpdateProductRequest request) {
        return rateLimitingGrpcCallExecutor.execute(
                () -> productGrpcClient.updateProduct(request),
                rateLimiters
        );
    }
}
