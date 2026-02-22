package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductTradeServiceImpl implements ProductTradeService {

    private final ObjectFactory<ProductTradeServiceAPIGrpc.ProductTradeServiceAPIBlockingStub>
            productTradeServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<ProductTradeInfo> fetchOne(String playerUuid, String tradeUuid) {
        GetProductTradeRequest request = GetProductTradeRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setTradeUuid(tradeUuid)
                .build();

        return Mono.fromCallable(() ->
                        productTradeServiceAPIBlockingStubObjectFactory.getObject().getProductTrade(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() ->
                        productTradeServiceAPIBlockingStubObjectFactory.getObject().getProductTrades(request))
                .subscribeOn(Schedulers.boundedElastic())
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

        return Mono.fromCallable(() ->
                        productTradeServiceAPIBlockingStubObjectFactory.getObject().createProductTrade(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC createProductTrade start playerUuid={} productId={}", playerUuid, productId))
                .doOnSuccess(resp -> log.info("gRPC createProductTrade success playerUuid={} productId={}", playerUuid, productId))
                .doOnError(e -> log.error("gRPC createProductTrade error playerUuid={} productId={} err={}", playerUuid, productId, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде