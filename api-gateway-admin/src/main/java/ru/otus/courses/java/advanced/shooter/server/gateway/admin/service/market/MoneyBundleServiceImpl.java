package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.MoneyBundleGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.MoneyBundleGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

@Slf4j
@Service
public class MoneyBundleServiceImpl implements MoneyBundleService {

    private final MoneyBundleGrpcClient moneyBundleGrpcClient;
    private final Scheduler schedulerMarket;

    public MoneyBundleServiceImpl(
            @Qualifier(MoneyBundleGrpcClientRateLimitingWrapper.NAME) MoneyBundleGrpcClient moneyBundleGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler schedulerMarket
    ) {
        this.moneyBundleGrpcClient = moneyBundleGrpcClient;
        this.schedulerMarket = schedulerMarket;
    }

    @Override
    public Mono<MoneyBundleInfo> fetchOne(int id) {
        GetMoneyBundleRequest request = GetMoneyBundleRequest.newBuilder()
                .setId(id)
                .build();

        return Mono.fromCallable(() -> moneyBundleGrpcClient.getEnabledMoneyBundle(request))
                .subscribeOn(schedulerMarket)
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
                .subscribeOn(schedulerMarket)
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
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC getMoneyBundles start paginationRequest={} currencyId={}", paginationRequest, currencyId))
                .doOnSuccess(resp -> log.info("gRPC getMoneyBundles success paginationRequest={}  currencyId={}", paginationRequest, currencyId))
                .doOnError(e -> log.error("gRPC getMoneyBundles error paginationRequest={}  currencyId={} err={}", paginationRequest, currencyId, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleInfo> create(MoneyBundleWritableData data) {
        CreateMoneyBundleRequest request = CreateMoneyBundleRequest.newBuilder()
                .setData(data)
                .build();

        return Mono.fromCallable(() -> moneyBundleGrpcClient.createMoneyBundle(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC createMoneyBundle start data={}", data))
                .doOnSuccess(resp -> log.info("gRPC createMoneyBundle success data={}", data))
                .doOnError(e -> log.error("gRPC createMoneyBundle error data={} err={}", data, e.getMessage(), e));
    }

    @Override
    public Mono<MoneyBundleInfo> update(int id, int version, MoneyBundleWritableData data) {
        UpdateMoneyBundleRequest request = UpdateMoneyBundleRequest.newBuilder()
                .setData(data)
                .setId(id)
                .setVersion(version)
                .build();

        return Mono.fromCallable(() -> moneyBundleGrpcClient.updateMoneyBundle(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC updateMoneyBundle start data={}", data))
                .doOnSuccess(resp -> log.info("gRPC updateMoneyBundle success data={}", data))
                .doOnError(e -> log.error("gRPC updateMoneyBundle error data={} err={}", data, e.getMessage(), e));
    }
}