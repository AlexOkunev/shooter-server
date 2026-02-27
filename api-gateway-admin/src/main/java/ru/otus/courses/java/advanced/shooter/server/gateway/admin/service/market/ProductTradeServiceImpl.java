package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.ProductTradeGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.ProductTradeGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.*;

@Slf4j
@Service
public class ProductTradeServiceImpl implements ProductTradeService {

    private final ProductTradeGrpcClient productTradeGrpcClient;
    private final Scheduler schedulerMarket;

    public ProductTradeServiceImpl(
            @Qualifier(ProductTradeGrpcClientRateLimitingWrapper.NAME) ProductTradeGrpcClient productTradeGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler schedulerMarket
    ) {
        this.productTradeGrpcClient = productTradeGrpcClient;
        this.schedulerMarket = schedulerMarket;
    }

    @Override
    public Mono<ProductTradeInfo> fetchOne(String playerUuid, String tradeUuid) {
        GetProductTradeRequest request = GetProductTradeRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setTradeUuid(tradeUuid)
                .build();

        return Mono.fromCallable(() -> productTradeGrpcClient.getProductTrade(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC getProductTrade start playerUuid={} tradeUuid={}", playerUuid, tradeUuid))
                .doOnSuccess(resp -> log.info("gRPC getProductTrade success playerUuid={} tradeUuid={}", playerUuid, tradeUuid))
                .doOnError(e -> log.error("gRPC getProductTrade error playerUuid={} tradeUuid={} err={}", playerUuid, tradeUuid, e.getMessage(), e));
    }

    @Override
    public Mono<ProductTradeInfoListPage> fetchPage(String playerUuid, GetProductTradesRequest.Filter filter, PaginationRequest paginationRequest) {
        GetProductTradesRequest request = GetProductTradesRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(filter)
                .setPlayerUuid(playerUuid)
                .build();

        return Mono.fromCallable(() -> productTradeGrpcClient.getProductTrades(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC getProductTrades start playerUuid={} paginationRequest={}", playerUuid, paginationRequest))
                .doOnSuccess(resp -> log.info("gRPC getProductTrades success playerUuid={} paginationRequest={}", playerUuid, paginationRequest))
                .doOnError(e -> log.error("gRPC getProductTrades error playerUuid={} paginationRequest={} err={}", playerUuid, paginationRequest, e.getMessage(), e));
    }

    @Override
    public Mono<ProductTradeInfo> createTrade(String playerUuid, int productId) {
        CreateProductTradeRequest request = CreateProductTradeRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setProductId(productId)
                .build();

        return Mono.fromCallable(() -> productTradeGrpcClient.createProductTrade(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC createProductTrade start playerUuid={} productId={}", playerUuid, productId))
                .doOnSuccess(resp -> log.info("gRPC createProductTrade success playerUuid={} productId={}", playerUuid, productId))
                .doOnError(e -> log.error("gRPC createProductTrade error playerUuid={} productId={} err={}", playerUuid, productId, e.getMessage(), e));
    }
}