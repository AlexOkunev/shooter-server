package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.ProductGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.ProductGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductGrpcClient productGrpcClient;
    private final Scheduler schedulerMarket;

    public ProductServiceImpl(
            @Qualifier(ProductGrpcClientRateLimitingWrapper.NAME) ProductGrpcClient productGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler schedulerMarket
    ) {
        this.productGrpcClient = productGrpcClient;
        this.schedulerMarket = schedulerMarket;
    }

    @Override
    public Mono<ProductInfo> fetchOne(int id) {
        GetProductRequest request = GetProductRequest.newBuilder()
                .setId(id)
                .build();

        return Mono.fromCallable(() -> productGrpcClient.getEnabledProduct(request))
                .subscribeOn(schedulerMarket)
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
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC getProducts start paginationRequest={}", paginationRequest))
                .doOnSuccess(resp -> log.info("gRPC getProducts success paginationRequest={}", paginationRequest))
                .doOnError(e -> log.error("gRPC getProducts error paginationRequest={} err={}", paginationRequest, e.getMessage(), e));
    }

    @Override
    public Mono<ProductInfo> create(ProductWritableData data) {
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> productGrpcClient.createProduct(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC createProduct start data={}", data))
                .doOnSuccess(resp -> log.info("gRPC createProduct success data={}", data))
                .doOnError(e -> log.error("gRPC createProduct error data={} err={}", data, e.getMessage(), e));
    }

    @Override
    public Mono<ProductInfo> update(int id, int version, ProductWritableData data) {
        UpdateProductRequest request = UpdateProductRequest.newBuilder()
                .setData(data)
                .setProductId(id)
                .setVersion(version)
                .build();

        return Mono.fromCallable(() -> productGrpcClient.updateProduct(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC updateProduct start data={}", data))
                .doOnSuccess(resp -> log.info("gRPC updateProduct success data={}", data))
                .doOnError(e -> log.error("gRPC updateProduct error data={} err={}", data, e.getMessage(), e));
    }
}