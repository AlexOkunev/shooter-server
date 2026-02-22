package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneyBundleTradeServiceImpl implements MoneyBundleTradeService {

    private final ObjectFactory<MoneyBundleTradeServiceAPIGrpc.MoneyBundleTradeServiceAPIBlockingStub>
            moneyBundleTradeServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<MoneyBundleTradeInfo> fetchOne(String playerUuid, String tradeUuid) {
        GetMoneyBundleTradeRequest request = GetMoneyBundleTradeRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setTradeUuid(tradeUuid)
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundleTrade(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundleTrade start playerUuid={} tradeUuid={}", playerUuid, tradeUuid))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundleTrade success playerUuid={} tradeUuid={}", playerUuid, tradeUuid))
                .doOnError(e -> log.error("gRPC getMoneyBundleTrade error playerUuid={} tradeUuid={} err={}", playerUuid, tradeUuid, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleTradeInfoListPage> fetchPage(String playerUuid, GetMoneyBundleTradesRequest.Filter filter, PaginationRequest paginationRequest) {
        GetMoneyBundleTradesRequest request = GetMoneyBundleTradesRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(filter)
                .setPlayerUuid(playerUuid)
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundleTrades(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundleTrades start playerUuid={} paginationRequest={}", playerUuid, paginationRequest))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundleTrades success playerUuid={} paginationRequest={}", playerUuid, paginationRequest))
                .doOnError(e -> log.error("gRPC getMoneyBundleTrades error playerUuid={} paginationRequest={} err={}", playerUuid, paginationRequest, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleTradeInfo> createTrade(String playerUuid, int moneyBundleId) {
        CreateMoneyBundleTradeRequest request = CreateMoneyBundleTradeRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setMoneyBundleId(moneyBundleId)
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().createMoneyBundleTrade(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC createMoneyBundleTrade start playerUuid={} moneyBundleId={}", playerUuid, moneyBundleId))
                .doOnSuccess(resp -> log.info("gRPC createMoneyBundleTrade success playerUuid={} moneyBundleId={}", playerUuid, moneyBundleId))
                .doOnError(e -> log.error("gRPC createMoneyBundleTrade error playerUuid={} moneyBundleId={} err={}", playerUuid, moneyBundleId, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleTradeInfo> performPayment(String playerUuid, String tradeUuid) {
        PerformMoneyBundleTradePaymentRequest request = PerformMoneyBundleTradePaymentRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setTradeUuid(tradeUuid)
                .build();

        return Mono.fromCallable(() ->
                        moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().performMoneyBundleTradePayment(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC performMoneyBundleTradePayment start playerUuid={} tradeUuid={}", playerUuid, tradeUuid))
                .doOnSuccess(resp -> log.info("gRPC performMoneyBundleTradePayment success playerUuid={} tradeUuid={}", playerUuid, tradeUuid))
                .doOnError(e -> log.error("gRPC performMoneyBundleTradePayment error playerUuid={} tradeUuid={} err={}", playerUuid, tradeUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде