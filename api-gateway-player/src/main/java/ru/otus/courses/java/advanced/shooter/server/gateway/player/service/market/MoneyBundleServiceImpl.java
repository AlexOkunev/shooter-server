package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.MoneyBundleGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.MoneyBundleGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.GetMoneyBundleRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.GetMoneyBundlesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfoListPage;

@Slf4j
@Service
public class MoneyBundleServiceImpl implements MoneyBundleService {

    private final MoneyBundleGrpcClient moneyBundleGrpcClient;
    private final Scheduler marketScheduler;

    public MoneyBundleServiceImpl(
            @Qualifier(MoneyBundleGrpcClientRateLimitingWrapper.NAME) MoneyBundleGrpcClient moneyBundleGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler marketScheduler
    ) {
        this.moneyBundleGrpcClient = moneyBundleGrpcClient;
        this.marketScheduler = marketScheduler;
    }

    @Override
    public Mono<MoneyBundleInfo> fetchOne(int id) {
        GetMoneyBundleRequest request = GetMoneyBundleRequest.newBuilder()
                .setId(id)
                .build();

        return Mono.fromCallable(() -> moneyBundleGrpcClient.getEnabledMoneyBundle(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getEnabledMoneyBundle start id={}", id))
                .doOnSuccess(resp -> log.info("gRPC getEnabledMoneyBundle success id={}", id))
                .doOnError(e -> log.error("gRPC getEnabledMoneyBundle error id={} err={}", id, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleInfoListPage> fetchMoneyBundlesPage(PaginationRequest paginationRequest) {
        GetMoneyBundlesRequest request = GetMoneyBundlesRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(GetMoneyBundlesRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .setCurrencyEnabled(true)
                        .setCurrencyCanBeBought(true)
                        .build()
                )
                .build();

        return Mono.fromCallable(() -> moneyBundleGrpcClient.getMoneyBundles(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundles start paginationRequest={}", paginationRequest))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundles success paginationRequest={}", paginationRequest))
                .doOnError(e -> log.error("gRPC getMoneyBundles error paginationRequest={} err={}", paginationRequest, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleInfoListPage> fetchMoneyBundlesPageByCurrencyId(int currencyId, PaginationRequest paginationRequest) {
        GetMoneyBundlesRequest request = GetMoneyBundlesRequest.newBuilder()
                .setPaginationRequest(paginationRequest)
                .setFilter(GetMoneyBundlesRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .setCurrencyEnabled(true)
                        .setCurrencyCanBeBought(true)
                        .addCurrencyIds(currencyId)
                        .build()
                )
                .build();

        return Mono.fromCallable(() -> moneyBundleGrpcClient.getMoneyBundles(request))
                .subscribeOn(marketScheduler)
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundles start paginationRequest={} currencyId={}", paginationRequest, currencyId))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundles success paginationRequest={}  currencyId={}", paginationRequest, currencyId))
                .doOnError(e -> log.error("gRPC getMoneyBundles error paginationRequest={}  currencyId={} err={}", paginationRequest, currencyId, e.getMessage(), e));
    }
}