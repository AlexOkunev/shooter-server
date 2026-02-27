package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.ProductGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.ProductGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.GetProductRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.GetProductsRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductGrpcClient productGrpcClient;
    private final Scheduler marketScheduler;

    public ProductServiceImpl(
            @Qualifier(ProductGrpcClientRateLimitingWrapper.NAME) ProductGrpcClient productGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler marketScheduler
    ) {
        this.productGrpcClient = productGrpcClient;
        this.marketScheduler = marketScheduler;
    }

    @Override
    public Mono<ProductInfo> fetchOne(int id) {
        GetProductRequest request = GetProductRequest.newBuilder()
                .setId(id)
                .build();

        return Mono.fromCallable(() -> productGrpcClient.getEnabledProduct(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getEnabledProduct start id={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledProduct success id={}", id))
                .doOnError(e -> log.error("gRPC getEnabledProduct error id={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<ProductInfoListPage> fetchProductsPage(PaginationRequest paginationRequest) {
        GetProductsRequest request = GetProductsRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(GetProductsRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .setEquipmentEnabled(true)
                        .setPriceCurrencyEnabled(true)
                        .build()
                )
                .build();

        return Mono.fromCallable(() -> productGrpcClient.getProducts(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getProducts start paginationRequest={}", paginationRequest))
                .doOnSuccess(resp -> log.info("gRPC getProducts success paginationRequest={}", paginationRequest))
                .doOnError(e -> log.error("gRPC getProducts error paginationRequest={} err={}", paginationRequest, e.getMessage(), e));
    }
}